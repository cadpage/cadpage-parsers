package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNRheaCountyCParser extends FieldProgramParser {
  
  public TNRheaCountyCParser() {
    super("RHEA COUNTY", "TN", 
          "ID CALL ADDRCITY EMPTY! END");
  }
  
  @Override
  public String getFilter() {
    return "rheacotn@911email.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD INCIDENT")) return false;
    return parseFields(body.split("\\|", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CAD *#(\\d{8}-\\d{6}):", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern ADDR_ID_GPS_PTN = Pattern.compile("([^\\[\\(]*)(?:\\[(\\d+)\\] *)?(?:\\((.*)\\))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_ID_GPS_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCallId = getOptGroup(match.group(2));
        String gps = match.group(3);
        if (gps != null) setGPSLoc(gps, data);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ID GPS";
    }
  }
}
