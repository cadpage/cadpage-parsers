package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;

/**
 * Carteret county, NC
 */
public class NCCarteretCountyParser extends DispatchSouthernPlusParser {

  public NCCarteretCountyParser() {
    super(CITY_LIST, "CARTERET COUNTY", "NC",
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE2|DSFLG_OPT_CODE|DSFLG_OPT_ID|DSFLG_TIME);
    setupCallList(CALL_LIST);
  }

  @Override
  public String getFilter() {
    return "@carteretcountygov.org,@sealevelfire-rescue.org,vtext.com@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{3}) +(.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!super.parseMsg(subject, body, data)) return false;

    // Rule out non-Southern semicolon delimited field
    if (data.strPlace.contains(";")) return false;

    // Split code and call
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }

    // Occasionally they put the city name at the end of both streets in an
    // intersection, which needs to be fixed up.
    if (data.strPlace.startsWith("/ ") && data.strCity.length() > 0 && data.strPlace.endsWith(data.strCity)) {
      data.strAddress = append(data.strAddress, " & ", data.strPlace.substring(2,data.strPlace.length()-data.strCity.length()).trim());
      data.strPlace = "";
    }

    // Fix misspelled cities
    String fixCity = MISSPELLED_CITIES.getProperty(data.strCity.toUpperCase());
    if (fixCity != null) data.strCity = fixCity;

    // Sometimes city name is duplicated in address
    // which ends up as the place name
    if (data.strCity.equals(data.strPlace) ||
        data.strPlace.equals("BEAUFORT") ||
        data.strPlace.equals("SMYRNA")) data.strPlace = "";

    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_CROSS_FOLLOWS;
  }

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "MILLCREEK",      "MILL CREEK",
      "ONLSOW CO",      "ONSLOW CO",
      "PELLETIER",      "PELETIER"
  });

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP",
      "ABDOMINAL PAIN - PROBLEMS",
      "ALARM BURGLARY",
      "ALARM FIRE",
      "ALARM HOLD UP - PANIC",
      "ALARM MEDICAL",
      "ALLERGIES - REACTION",
      "ASSAULT IP",
      "ASSIST OTH AGENCY",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "BURGLARY NIP",
      "CARDIAC ARREST",
      "CHECK WELFARE",
      "CHEST PAIN",
      "DECEASED PERSON",
      "DIABETIC PROB",
      "DIST - NUIS - FIGHT IP",
      "ELEVATOR - ESCALATOR RESCUE",
      "FALL",
      "FALLS",
      "FIRE GRASS - BRUSH - WOODS - OUTSIDE FIRE",
      "FOOT PATROL",
      "GAS LEAK - ODOR (LP or Natural Gas)",
      "HEART PROB -DEFIB",
      "HEART PROB - DEFIB PROB",
      "HEAT - COLD EXPOSURE",
      "HEMORRHAGE - LACERATIONS",
      "LOCK IN - OUT URGENT",
      "LOUD NOISE - MUSIC/BARKING/PARTY",
      "MARINE FIRE",
      "MENTAL DISORDER/BEHAVIORAL PROBLEMS",
      "MVC MINOR",
      "MVC PI OR ROLLOVER",
      "MVC UNK PI",
      "OUTSIDE FIRE",
      "OVERDOSE - POISONING",
      "PSYCHIATRIC - ABNORMAL BHAVIOR - SUICIDE ATTEMPT",
      "REQ FOR SERV - EMS",
      "SICK PERSON",
      "STAB - GUNSHOT - PENETRATING TRAUMA",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDAL IP",
      "SUSP - WANT PERSON",
      "TRAFFIC HAZARD - ROAD RAGE",
      "TRAFFIC STOP",
      "TRANSFER (MEDICAL) INTERFACILITY",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS - FAINTING",
      "VEHICLE FIRE",
      "WATERCRAFT IN DISTRESS"
  );

  static final String[] CITY_LIST = new String[]{
    "ATLANTIC BEACH",
    "BEAUFORT",
    "BOGUE",
    "CAPE CARTERET",
    "CEDAR POINT",
    "EMERALD ISLE",
    "INDIAN BEACH",
    "MOREHEAD CITY",
    "NEWPORT",
    "PELETIER",
    "PELLETIER",   // Misspelled
    "PINE KNOLL SHORES",

    "ATLANTIC",
    "BETTIE",
    "BROAD CREEK",
    "CEDAR ISLAND",
    "DAVIS",
    "GALES CREEK",
    "GLOUCESTER",
    "HARKERS ISLAND",
    "HARLOWE",
    "LOLA",
    "MARSHALLBERG",
    "MERRIMON",
    "MILL CREEK",
    "MILLCREEK",
    "NORTH RIVER",
    "OCEAN",
    "OTWAY",
    "SALTER PATH",
    "SEA GATE",
    "SEA LEVEL",
    "STACY",
    "STELLA",
    "STRAITS",
    "SMYRNA",
    "WILDWOOD",
    "WILLISTON",
    "WIREGRASS",

    // Craven County
    "CRAVEN CO",
    "CRAVEN",
    "HAVELOCK",

    // Jones County
    "JONES CO",
    "JONES",
    "MAYSVILLE",

    // Lenoir County
    "LENOIR CO",
    "LENOIR",
    "KINSTON",

    // Onslow County
    "ONSLOW CO",
    "ONLSOW CO",
    "ONSLOW COUNTY",
    "ONSLOW",
    "HUBERT",
    "SWANSBORO",
    "WHITE OAK"
  };
}
