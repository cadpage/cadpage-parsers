package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXRockwallCountyCParser extends HtmlProgramParser {
  
  public TXRockwallCountyCParser() {
    super("ROCKWALL COUNTY", "TX", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! ID:ID! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@rockwall.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("\\*+\\d\\d?/\\d\\d?/\\d{4}\\*+|\\d\\d?:\\d\\d:\\d\\d");
  private class MyInfoField extends InfoField {
    
    private boolean enabled = false;
    
    @Override
    public void parse(String field, Data data) {
      if (INFO_DATE_TIME_PTN.matcher(field).matches()) {
        enabled = false;
        return;
      }
      if (field.equals("-")) {
        enabled = true;
        return;
      }
      
      if (enabled) data.strSupp = append(data.strSupp, "\n", field);
    }
  }

}
