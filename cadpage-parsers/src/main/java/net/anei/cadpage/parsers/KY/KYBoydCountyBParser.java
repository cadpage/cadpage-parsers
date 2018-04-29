package net.anei.cadpage.parsers.KY;

import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class KYBoydCountyBParser extends SmartAddressParser {
  
  public KYBoydCountyBParser() {
    super(CITY_LIST, "BOYD COUNTY", "KY");
    setupCallList(CALL_LIST);
    setupSpecialStreets(SPECIAL_STREET_LIST);
    removeWords("ALLEY");
    setupMultiWordStreets(MWORD_STREET_LIST);
    setFieldList("UNIT CODE CALL ADDR CITY ID");
  }
  
  private static final Pattern MASTER = Pattern.compile("CISCAD:([A-Z0-9 ]+) ([A-Z]{2,5}) - (.*) (\\d{12})");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = match.group(1).trim();
    data.strCode = match.group(2);
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, match.group(3).trim(), data);
    data.strCallId = match.group(4);
    
    data.strAddress = fixAddress(data.strAddress);
    return true;
  }
  
  static String fixAddress(String addr) {
    Matcher match = STREET_FIX_PTN.matcher(addr);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        String street = match.group().toUpperCase();
        String replace = STREET_FIX_TABLE.getProperty(street);
        if (replace == null) replace = STREET_FIX_TABLE.getProperty(street + " RD");
        if (replace != null) street = replace;
        match.appendReplacement(sb, street);
      } while (match.find());
      match.appendTail(sb);
      addr = sb.toString();
    }
    return addr;
  }

  // Table of street abbreviations that need to be expanded.
  // Streets abbreviations ending with RD will match a text string with or without
  // the RD suffix
  private static final Properties STREET_FIX_TABLE = buildCodeTable(new String[]{
      "ALLEY BR RD",           "Alley Branch Rd",
      "FRK ST",                "Fork Rd",
      "JOHNSONS FRK RD",       "Johnsons Fork Rd",
      "STRAIGHT CRK RD",       "Straight Creek Rd",
      "WHITES CRK RD",         "Whites Creek Rd"
  });
  
  // Master street abbreviation pattern.  Built at class load time from STREET_FIX_TABLE.
  private static Pattern STREET_FIX_PTN;
  static {
    StringBuilder sb = new StringBuilder("\\b(");
    boolean first = true;
    Enumeration<?> e = STREET_FIX_TABLE.propertyNames();
    while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      if (first) first = false;
      else sb.append('|');
      sb.append(key);
      if (key.endsWith(" RD")) {
        sb.append('|');
        sb.append(key.substring(0,key.length()-3));
      }
    }
    sb.append(")\\b");
    STREET_FIX_PTN = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
  }

  @Override
  public String adjustMapCity(String city) {
    String mapCity = CITY_TABLE.getProperty(city.toUpperCase());
    if (mapCity != null) city = mapCity;
    return super.adjustMapCity(city);
  }
  
  static final String[] SPECIAL_STREET_LIST = new String[]{
      "BARBAQUE SPUR",
      "COURT OF THREE SISTERS"
  };
  
  static final String[] MWORD_STREET_LIST = new String[]{
      "APPLE BLOSSOM",
      "BEAR CREEK",
      "BELLEFONTE PRINCESS",
      "BIG RUN",
      "BOOTH QUILLEN",
      "BOY SCOUT",
      "BUENA VISTA",
      "CARL PERKINS",
      "CEDAR KNOLL",
      "CHESTNUT HILL",
      "CLAY JACK",
      "DALE RAY",
      "DOG RDG ROBERTS",
      "FOUR MILE",
      "FRENCH BROAD",
      "GRAYDON HEIGHTS",
      "GRAYSON HEIGHTS",
      "HENRY CLAY",
      "HICKORY HILLS",
      "HOODS CREEK",
      "HUFF HOLLOW",
      "IKE PATTON",
      "JOE MARCUM",
      "JOHNSONS FORK",
      "KIRBY FLATS",
      "LAKE BONITA",
      "LAKEWOOD VILLAGE",
      "LITTLE GARNER",
      "LONE OAK",
      "MARSH HILL",
      "MARTIN LUTHER KING",
      "MEADE SPRINGER",
      "RADIO PARK",
      "ROWE CREEK",
      "SOUTHERN HILLS",
      "STRAIGHT CREEK",
      "TWIN RIDGE",
      "WHITE OAK"
  };
  
  static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "BIG SANDY",       "CATLETTSBURG",
      "EAST FORK",       "RUSH",
      "ENGLAND HILL",    "CATLETTSBURG",
      "STRAIGHT CREEK",  "ASHLAND"
  });
  
  static final String[] CITY_LIST = new String[]{

    // Cities
    "ASHLAND",
    "CATLETTSBURG",

    // Census-designated places
    "CANNONSBURG",
    "WESTWOOD",

    // Unincorporated communities
    "BURNAUGH",
    "COALTON",
    "DURBIN",
    "IRONVILLE",
    "KAVANAUGH",
    "LOCKWOOD",
    "MEADS",
    "NORMAL",
    "PRINCESS",
    "ROCKDALE",
    "RUSH",
    "SUMMIT",
    "SUMMITT", 
    "UNITY",
    
    // Neighborhoods
    "BIG SANDY",
    "EAST FORK",
    "ENGLAND HILL",
    "STRAIGHT CREEK",
  };
  
  static final CodeSet CALL_LIST = new CodeSet(
      "ABANDONED VEHICLE",
      "ACCIDENT-INJURY",
      "ACCIDENT-INJURY*",
      "ACCIDENT-INJURY ROAD CLEAR",
      "ACCIDENT-NO INJURY",
      "ALARM-RESIDENTIAL BURGLARY",
      "ALARM-UNKNOWN",
      "ASSIST-EMERGENCY",
      "ASSIST-NON EMERGENCY",
      "ATTEMPT TO LOCATE",
      "CHECK WELFARE",
      "CRIMINAL MISCHIEF-SUSPECT FLED",
      "DOMESTIC-INPROGRESS",
      "EM-INDUSTRIAL RELEASE",
      "EMS-BACK PAIN",
      "EMS-BLEEDING /LACERATIONS",
      "EMS-BLOOD PRESSURE PROB",
      "EMS-BLOOD PRESSURE PROBLEMS",
      "EMS-BURN",
      "EMS-CARDIAC ARREST",
      "EMS-CHEST INJURY",
      "EMS-CHEST PAIN",
      "EMS-DIABETIC PROBLEM",
      "EMS-DRUG ABUSE OVERDOSE",
      "EMS-FALL",
      "EMS-HEART PROBLEMS",
      "EMS-INJURY/TRAUMA",
      "EMS-LIFTING ASSISTANCE",
      "EMS-MEDICAL ALARM",
      "EMS-MEDICAL EMERGENCY",
      "EMS-PAIN/SWELLING",
      "EMS-PSYCH PATIENT",
      "EMS-REACTION/ALLERGIC",
      "EMS-SEIZURE",
      "EMS-SHORTNESS OF BREATH",
      "EMS-SICK PERSON/GEN ILLNE",
      "EMS-SICK PERSON/GEN ILLNESS",
      "EMS-STOMACH PAINS",
      "EMS-STROKE",
      "EMS-SEIZURE",
      "EMS-UNCONSCIOUS/PASSED OUT",
      "EMS-UNRESPONSIVE",
      "EMS-TRANSFER",
      "FIGHT-IN PROGRESS",
      "FIGHT-VERBAL ONLY",
      "FIRE-ALARM",
      "FIRE-ALARM COMMERCIAL",
      "FIRE-BRUSH FIRE",
      "FIRE-ELECTRICAL",
      "FIRE-FUEL SPILL/LEAK",
      "FIRE-HAZ MAT",
      "FIRE-MUTUAL AID",
      "FIRE-PUBLIC ASSIST",
      "FIRE-RESCUE",
      "FIRE-RESIDENTIAL LOCKOUT",
      "FIRE-STRUCTURE",
      "FIRE-STRUCTURE COMMERICIL",
      "FIRE-TRASH/DUMP",
      "FIRE-TREE REMOVAL",
      "FIRE-UNKNOWN SMOKE / AREA",
      "FIRE-UNK SMOKE IN STRUCT",
      "FIRE-UTILITY LINE/TRANSFORMER",
      "FIRE-VEHICLE",
      "MISSING JUVENILE",
      "MISSING PERSON",
      "MISSING OVER 18YOA",
      "RUNAWAY-1-8HRS",
      "SECURITY-OFF DUTY",
      "SUICIDE-THREATS/ATTEMPT",
      "SUICIDE-THREATS/ATTEMPT*",
      "SUSP CIRCUMSTANCES",
      "SUSP CIRCUMSTANCES-1-8HRS",
      "THEFT-OVER 8HRS",
      "TRAINING-WEEKLY PT",
      "UNATTENDED DEATH",
      "WEAPON VIOLATION-DISCHARGING"
  );
}
