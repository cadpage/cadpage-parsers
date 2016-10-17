package net.anei.cadpage.parsers.MO;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class MOBarryCountyAParser extends DispatchB2Parser {
  
  private static final Pattern  WEATHER_PTN = Pattern.compile("BC911:(WEATHR>WEATHER .*?)(?: Cad: ([-\\d]+))?");
  private static final Pattern FARM_ROAD_PTN = Pattern.compile("\\bFARM ROAD\\b");
  private static final Pattern FR_PTN = Pattern.compile("\\bFR\\b");
 
  public MOBarryCountyAParser() {
    super("BC911:", MOBarryCountyParser.CITY_LIST, "BARRY COUNTY", "MO");
    setupCallList(CALL_LIST);
    setupGpsLookupTable(MOBarryCountyParser.GPS_LOOKUP_TABLE);
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_KEEP_STATE_HIGHWAY | MAP_FLG_SUPPR_CR;
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address, String apt) {
    return MOBarryCountyParser.fixGpsLookupAddress(address, apt);
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Weather reports are occasionally mistaken for dispatch calls if we
    // do not catch them here
    Matcher match = WEATHER_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "GENERAL ALERT";
      data.strPlace = match.group(1).trim();
      data.strCallId = match.group(2);
      return true;
    }
    
    String save = body;
    body = FARM_ROAD_PTN.matcher(body).replaceAll("FR");
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equalsIgnoreCase("BARRY COUNTY")) data.strCity = "";
    
    if (save.length() != body.length()) {
      data.strAddress = FR_PTN.matcher(data.strAddress).replaceAll("FARM ROAD");
      data.strCross = FR_PTN.matcher(data.strCross).replaceAll("FARM ROAD");
    }
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP",
      "911 NEWTON CO",
      "ALARMS",
      "ASSIST",
      "ASSIST A PERSON",
      "ASSIST OTHER AGENCY",
      "BRUSH FIRE",
      "CHECK PERSON",
      "CHECK WELL BEING",
      "DOMESTIC",
      "FIRE OTHER",
      "GENERAL INFO",
      "MEDICAL CALL",
      "MOTOR VEH ACC UNKNOWN INJ",
      "MOTOR VEHICLE ACCIDENT",
      "STRUCTURE FIRE",
      "TEST",
      "VEHICLE FIRE"
  );
}