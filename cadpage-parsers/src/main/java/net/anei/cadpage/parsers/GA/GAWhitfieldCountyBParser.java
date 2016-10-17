package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class GAWhitfieldCountyBParser extends DispatchA48Parser {
  
  public GAWhitfieldCountyBParser() {
    super(CITY_LIST, "WHITFIELD COUNTY", "GA", FieldType.X_NAME,
          Pattern.compile("[A-Z]+\\d+|BATT"));
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "CROSS PLAINS",
        "KIMBERLY PARK"
    );
  }
  
  @Override
  public String getFilter() {
    return "PageGate@whitfieldcountyga.com,postmaster@whitfield.com";
  }
  
  private static final String[] CALL_LIST = new String[]{
    "BREATHING PROBLEMS",
    "ELECTRICAL & APPLIANCE FIRES",
    "FIRE ALARM",
    "M.V. ACCIDENT W/INJURIES",
    "M.V. ACCIDENT W/PEDESTRIAN",
    "OUTSIDE FIRE WITH EXPOSURES",
    "OUTSIDE FIRE(BRUSH,WOODS,ETC.)",
    "SF-COMMERCIAL/INDUSTRIAL/SCHOO",
    "VEHICLE FIRE - CMV"
  };
  
  private static final String[] CITY_LIST = new String[]{
      "COHUTTA",
      "DALTON",
      "RESACA",
      "ROCKY FACE",
      "TUNNEL HILL",
      "VARNELL"
  };
}
