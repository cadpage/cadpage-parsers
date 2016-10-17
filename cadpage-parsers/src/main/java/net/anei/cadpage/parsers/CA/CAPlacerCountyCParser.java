package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAPlacerCountyCParser extends FieldProgramParser {
  
  public CAPlacerCountyCParser() {
    super(CITY_LIST, "PLACER COUNTY", "CA", 
          "( SELECT/1 CALL! Address:ADDR! Common_Name:PLACE! XStreets:X! Additional_Location_Info:INFO! Assigned_Units:UNIT! Quadrant:MAP! District:MAP/S! Beat:MAP/S! Incident:ID! Narrative:INFO! " + 
          "| CALL ADDR CODE UNIT UNIT/CS INFO+? ID! )");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@roseville.ca.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.indexOf(" Address:") >= 0) {
      setSelectValue("1");
      return super.parseMsg(body, data);
    }  else {
      setSelectValue("2");
      return parseFields(body.split("\n"), data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}\\b.*", true);
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    
    public MyCallField() {
      super("[A-Z]+", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      data.strCall = convertCodes(field, CALL_CODES);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        String addr = field.substring(0,pt).replace('@',  '&');
        String city =field.substring(pt+1).trim();
        parseAddress(StartType.START_ADDR, FLAG_NO_CITY | FLAG_RECHECK_APT | FLAG_ANCHOR_END, 
                     addr, data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        if (data.strCity.length() == 0) abort();
        data.strPlace = getLeft();
      } else {
        field = field.replace('@', '&');
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT, field, data);
        data.strPlace = getLeft();
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "AUTOAID","Automatic Aid Request",
      "BOMB",   "Bomb Threat",
      "BOXCAR", "Boxcar Fire",
      "CALARM", "Commercial Fire Alarm",
      "CGAS",   "Commercial Gas Leak",
      "CO",     "Carbon Monoxide Alarm",
      "CSTRUH", "Commercial Structure (High)",
      "CSTRUL", "Commercial Structure (Low)",
      "ELEV",   "Elevator Rescue",
      "FINV",   "Fire Investigation",
      "FLOOD",  "Flooding",
      "FTRASH", "Trash Fire",
      "FVEH",   "Vehicle Fire",
      "HWIRE",  "Hazardous Wires",
      "HYD",    "Broken Hydrant",
      "HZH",    "Hazmat(High)",
      "HZL",    "Hazmat(Low)",
      "LAND",   "Helicopter Landing Zone",
      "LOCKH",  "Lock In (High)",
      "LOCKL",  "Lock In (Low)",
      "MAID",   "Medical Aid",
      "MUTAID", "Mutual Aid",
      "PAST",   "Public Assistance",
      "PLANE",  "Plane Crash",
      "POLICE", "Police Assist",
      "RALARM", "Residential Alarm",
      "RESCUE", "Rescue",
      "RGAS",   "Residential Gas Leak",
      "RSTRUH", "Residential Structure (High)",
      "RSTRUL", "Residential Structure (Low)",
      "STRIKE", "Strike Team Request",
      "TRAIN",  "Train Wreck / Derailment",
      "VAF",    "Vehicle Accident with Fire",
      "VAH",    "Vehicle Accident (High)",
      "VAL",    "Vehicle Accident (Low)",
      "VEGH",   "Vegetation Fire (High)",
      "VEGL",   "Vegetation Fire (Low)",
      "WFLOW",  "Water Flow"

  });
  
  private static final String[] CITY_LIST = new String[]{
    "ROSEVILLE"
  };
}
