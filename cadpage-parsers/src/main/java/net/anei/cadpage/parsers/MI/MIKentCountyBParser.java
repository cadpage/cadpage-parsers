package net.anei.cadpage.parsers.MI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIKentCountyBParser extends FieldProgramParser {
  
  public MIKentCountyBParser() {
    super("KENT COUNTY", "MI", 
          "Agency:SRC! Incident_Date:SKIP! Location:ADDR! Apartment:APT! Location_Name:PLACE! Cross_Steets:X! ITMC_Code:CODE! Inc_Type:CALL! M/C:CALL/SDS! All_Comments:INFO! INFO/N+ Units_Assigned:UNIT! City/Twp:CITY! A/S/B:MAP! Latitude:GPS1! Longitude:GPS2! Incident_Number:ID! Now_Time:DATETIME/d!");
  }
  
  @Override
  public String getFilter() {
    return "KCCCAlert@kent911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ \\|]*\\b\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
