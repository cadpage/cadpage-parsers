package net.anei.cadpage.parsers.KY;

import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYBoydCountyParser extends DispatchB2Parser {
  
  public KYBoydCountyParser() {
    super("BC911:", CITY_LIST, "BOYD COUNTY", "KY", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupSpecialStreets(
        "BARBAQUE SPUR",
        "COURT OF THREE SISTERS"
    );
    removeWords("ALLEY");
    setupMultiWordStreets(
        "BEAR CREEK",
        "BOY SCOUT",
        "CARL PERKINS",
        "CHESTNUT HILL",
        "DOG RDG ROBERTS",
        "HICKORY HILLS",
        "HOODS CREEK",
        "HUFF HOLLOW",
        "KIRBY FLATS",
        "LAKE BONITA",
        "MARTIN LUTHER KING",
        "MEADE SPRINGER",
        "TWIN RIDGE"
    );
  }
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    int pt = field.indexOf("* ");
    if (pt >= 0) {
      field = field.substring(0,pt)+" @ "+field.substring(pt+2);
    }
    field = fixAddress(field);
    return super.parseAddrField(field, data);
  }
  
  private static String fixAddress(String addr) {
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
      "ALLEY BR RD",           "ALLEY BRANCH RD",
      "FRK ST",                "FORK RD",
      "JOHNSONS FRK RD",       "JOHNSONS FORK RD",
      "STRAIGHT CRK RD",       "STRAIGHT CREEK RD",
      "WHITES CRK RD",         "WHITES CREEK RD"
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
    city = convertCodes(city, CITY_TABLE);
    return super.adjustMapCity(city);
  }
  
  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "ENGLAND HILL",    "CATLETTSBURG",
  });
  
  private static final String[] CITY_LIST = new String[]{

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
    "ENGLAND HILL"
  };
  
  private CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT-INJURY",
      "ACCIDENT-INJURY ROAD CLEAR",
      "ACCIDENT-NO INJURY",
      "ALARM-UNKNOWN",
      "CHECK WELFARE",
      "DOMESTIC-INPROGRESS",
      "EMS-CARDIAC ARREST",
      "EMS-CHEST PAIN",
      "EMS-FALL",
      "EMS-LIFTING ASSISTANCE",
      "EMS-MEDICAL EMERGENCY",
      "EMS-PAIN/SWELLING",
      "EMS-SHORTNESS OF BREATH",
      "EMS-UNCONSCIOUS/PASSED OUT",
      "EMS-UNRESPONSIVE",
      "FIRE-ALARM",
      "FIRE-ALARM COMMERCIAL",
      "FIRE-BRUSH FIRE",
      "FIRE-ELECTRICAL",
      "FIRE-FUEL SPILL/LEAK",
      "FIRE-HAZ MAT",
      "FIRE-PUBLIC ASSIST",
      "FIRE-RESCUE",
      "FIRE-STRUCTURE",
      "FIRE-TRASH/DUMP",
      "FIRE-TREE REMOVAL",
      "FIRE-UNKNOWN SMOKE / AREA",
      "FIRE-UTILITY LINE/TRANSFORMER",
      "SUICIDE-THREATS/ATTEMPT",
      "SUSP CIRCUMSTANCES-1-8HRS",
      "TRAINING-WEEKLY PT",
      "WEAPON VIOLATION-DISCHARGING"
  );
}
