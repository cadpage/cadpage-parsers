package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXLubbockCountyAParser extends FieldProgramParser{
  
  private String version;

  public TXLubbockCountyAParser() {
    super("LUBBOCK COUNTY", "TX",
      "( SELECT/DISREGARD Location:ADDRCITY/S! | UNIT! Call_Sheet:SKIP! Location:ADDRCITY/S! Type_of_call:CALL! ) INFO+");
  }

  public String getFilter() {
    return "noreply@co.lubbock.tx.us";
  }

  @Override
  public String getProgram() {
    return append("ID CALL X", " ", getOptGroup(super.getProgram()))+" PLACE";
  }
  
  private static final Pattern SUBJECT_PATTERN
    = Pattern.compile("Lubbock County Dispatch: (\\d+)( Disregard)?");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    version = "";
    Matcher m = SUBJECT_PATTERN.matcher(subject);
    if (!m.matches()) return false;
    data.strCallId = m.group(1);
    if (m.group(2) != null) {
      data.strCall = version = "DISREGARD";
    }
    
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  protected String getSelectValue() {
    return version;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("([A-Z0-9]+) dispatched to:", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  private static final Pattern ADDRESS_PATTERN = Pattern.compile("(.*):(.*)");
  private class MyAddressCityField extends AddressCityField {    
    
    @Override
    public void parse(String field, Data data) {
      String aptField = "";
      Matcher m = ADDRESS_PATTERN.matcher(field);
      if (m.matches()) {
        field = m.group(1).trim();
        aptField = m.group(2).trim();
      }
      super.parse(field,  data);
      data.strApt = append(data.strApt, "-", aptField);
      
      if (data.strCity.equals("BUFFALO SPRINGS LAKE")) {
        data.strCity = "BUFFALO SPRINGS";
      } else if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = AVE_X_ST_PTN.matcher(addr).replaceAll("$1");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern AVE_X_ST_PTN = Pattern.compile("\\b(AVE [A-Z]) ST\\b");
}
