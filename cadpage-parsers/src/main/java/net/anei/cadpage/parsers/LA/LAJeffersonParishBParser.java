package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAJeffersonParishBParser extends FieldProgramParser {
  
  public LAJeffersonParishBParser() {
    super("JEFFERSON PARISH", "LA", 
          "Incident_ID:ID! Type:CALL! Location_Name:PLACE! Address:ADDR! Cross_Street:X! Latitude:GPS1/d! Longitude:GPS2/d! Division:MAP! Response_Area:MAP/L! Unit:UNIT! Time_Call_Entered:SKIP! Comment:INFO! END");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@kennerpd.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Message from CAD")) return false;
    body = body.replace(" Address;", " Address:")
               .replace("Response Area:", " Response Area:")
               .replace("TimeCall Entered:", " Time Call Entered:")
               .replace("Comment:", " Comment:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*)- *([A-Z0-9]+)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *, *(?=\\[\\d{1,2}\\] )");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      data.strSupp = field;
    }
  }
}
