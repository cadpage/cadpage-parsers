package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCCravenCountyEParser extends HtmlProgramParser {
  
  public NCCravenCountyEParser() {
    super("CRAVEN COUNTY", "NC", 
          "Response:SKIP! Call:CALL! Location:ADDRCITY! Common_Name:PLACE! Cross:X! Units:UNIT! Source:SRC! Date:DATETIME! Incident:ID! Narrative:EMPTY! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@havelocknc.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification: ")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("[Incident not yet created");
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}|\\d\\d?:\\d\\d:\\d\\d");
  private class MyInfoField extends InfoField {
    
    private boolean suppress = false;
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("http://maps.google.com")) return;
      if (suppress) {
        if (field.equals("-")) suppress = false;
        return;
      }
      
      if (INFO_DATE_TIME_PTN.matcher(field).matches()) {
        suppress = true;
        return;
      }
      
      super.parse(field, data);
    }
  }
  
}
