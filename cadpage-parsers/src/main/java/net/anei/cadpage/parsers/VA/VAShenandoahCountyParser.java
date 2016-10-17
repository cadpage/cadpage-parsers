package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class VAShenandoahCountyParser extends SmartAddressParser {
  
  private static final Pattern GPS_PTN = Pattern.compile(" *(?:(3[89]\\.\\d{4,} +-78\\.\\d{4,})|-361 +-361)\\b *");
  
  public VAShenandoahCountyParser() {
    super(CITY_LIST, "SHENANDOAH COUNTY", "VA");
    setupCallList(CALL_LIST);
    setFieldList("CALL PLACE ADDR APT GPS CITY UNIT X");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@shentel.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Information")) return false;
    Matcher match = GPS_PTN.matcher(body);
    if (!match.find()) return false;
    
    String addr = body.substring(0,match.start());
    String gps = match.group(1);
    String extra = body.substring(match.end());
    
    if (gps != null) setGPSLoc(gps, data);
    parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ | FLAG_START_FLD_NO_DELIM | FLAG_FALLBACK_ADDR | FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
    
    // Unit names like CO47 look like county roads, so we will tell the address parser that 
    // the city may be followed by cross streets
    parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY | FLAG_CROSS_FOLLOWS, extra, data);
    if (data.strCity.equalsIgnoreCase("COUNTY")) data.strCity = "";
    
    extra = getLeft();
    if (!isMBlankLeft()) {
      Parser p = new Parser(extra);
      data.strUnit = p.get("  ");
      extra = p.get();
    }
    if (!extra.equals("No Cross Streets Found")) data.strCross = extra;
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "Altered Mental Status",
      "ALTERED MENTAL STATUS",
      "Cardiac Arrest",
      "Cardiac Emergency",
      "CARDIAC EMERGENCY",
      "Choking",
      "CHOKING",
      "Diabetic Emergency",
      "DIABETIC EMERGENCY",
      "Difficulty Breathing",
      "DIFFICULTY BREATHING",
      "Fire Alarm",
      "FIRE ALARM",
      "General Illness",
      "GENERAL ILLNESS",
      "Injured Person",
      "INJURED PERSON",
      "Medical Alarm",
      "MEDICAL ALARM",
      "MVC",
      "Public Service",
      "PUBLIC SERVICE",
      "Suicide Attempt/Completed",
      "SUICIDE ATTEMPT/COMPLETED",
      "Vehicle Fire",
      "VEHICLE FIRE"
);
  
  private static final String[] CITY_LIST = new String[]{

    // Incorporated towns
    "STRASBURG",
    "WOODSTOCK",
    "NEW MARKET",
    "MOUNT JACKSON",
    "EDINBURG",
    "TOMS BROOK",

    // Unincorporated communities
    "ALONZAVILLE",
    "BASYE-BRYCE MOUNTAIN",
    "BOWMANS CROSSING",
    "CALVARY",
    "CARMEL",
    "CLARY",
    "COLUMBIA FURNACE",
    "CONICVILLE",
    "DETRICK",
    "FISHERS HILL",
    "FORESTVILLE",
    "HAMBURG",
    "HAWKINSTOWN",
    "LEBANON CHURCH",
    "MAURERTOWN",
    "MOUNT CLIFTON",
    "MOUNT OLIVE",
    "ORANDA",
    "ORKNEY SPRINGS",
    "QUICKSBURG",
    "SAINT LUKE",
    "SAUMSVILLE",
    "WHEATFIELD",
    
    // County names
    "COUNTY"
  };
}
