package net.anei.cadpage.parsers.WV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Hardy County, WV (B) 
 */
public class WVHardyCountyBParser extends DispatchA48Parser {
  
  public WVHardyCountyBParser() {
    super(CITY_LIST, "HARDY COUNTY", "WV", FieldType.X_NAME,
          Pattern.compile("[A-Z]\\d+[A-Z]?|\\d{4}"));
    setupCallList(CALL_CODE);
  }
  
  @Override
  public String getFilter() {
    return "Hardy911@hardynet.com,HARDYCOE911@hardynet.com";
  }
  
  private static final CodeSet CALL_CODE = new CodeSet(
      "ALLERGIC REACTION",
      "BREATHING PROBLEMS",
      "CARDIAC ARREST",
      "CHOKING",
      "DEAD ON ARRIVAL",
      "DOMESTIC REPORT",
      "DWELLING FIRE",
      "FALL",
      "FIRE ALARM",
      "FIRE GENERIC",
      "GAS LEAK",
      "GENERAL ILLNESS",   
      "MEDICAL ALARM",
      "MISSING PERSON REPORT",
      "MOTOR VEHICLE ACCIDENT", 
      "MUTUAL AID",
      "ORDER INVESTIGATION/COMPLAINT",
      "RUNAWAY/MISSING JUVENILE",
      "SMOKE INVESTIGATION",
      "TRANSPORT PRISONER",
      "TRAUMATIC INJURIES",
      "TREE DOWN",
      "UNCONSIOUS PERSON",
      "VEHICLE FIRE",
      "WIRES DOWN (NO FIRE)"
      
  );
  
  private static final String[] CITY_LIST = new String[]{

    "ARKANSAS",
    "BAKER",
    "BASORE",
    "BASS",
    "BAUGHMAN",
    "BEAN",
    "BRAKE",
    "CUNNINGHAM",
    "DURGON",
    "FISHER",
    "FLATS",
    "FORT RUN",
    "INKERMAN",
    "KESSEL",
    "LOST CITY",
    "LOST RIVER",
    "MATHIAS",
    "MCCAULEY",
    "MCNEILL",
    "MILAM",
    "MOOREFIELD",
    "NEEDMORE",
    "OLD FIELDS",
    "PERRY",
    "PERU",
    "RIG",
    "ROCK OAK",
    "ROCKLAND",
    "TANNERY",
    "TAYLOR",
    "WALNUT BOTTOM",
    "WARDENSVILLE"

  };
}
