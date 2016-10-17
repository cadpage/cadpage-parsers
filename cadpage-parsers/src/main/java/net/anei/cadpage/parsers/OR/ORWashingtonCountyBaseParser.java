package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

/**
 * Washington County, OR
 * Also Clackamas County
 */
abstract public class ORWashingtonCountyBaseParser extends FieldProgramParser {
  
  public ORWashingtonCountyBaseParser(String defCity, String defState, String program) {
    super(defCity, defState, program);
    addRoadSuffixTerms();
  }
  
  public ORWashingtonCountyBaseParser(String[] cityList, String defCity, String defState, String program) {
    super(cityList, defCity, defState, program);
    addRoadSuffixTerms();
  }
  
  public ORWashingtonCountyBaseParser(Properties cityCodes, String defCity, String defState, String program) {
    super(cityCodes, defCity, defState, program);
    addRoadSuffixTerms();
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    StringBuffer sb = new StringBuffer();
    Matcher match = XX_PTN.matcher(sAddress);
    while (match.find()) {
      match.appendReplacement(sb, convertCodes(match.group(), STREET_CODES));
    }
    match.appendTail(sb);
    return sb.toString();
  }
  private static final Pattern XX_PTN = Pattern.compile("\\b[A-Z]{2}\\b");

  private void addRoadSuffixTerms() {
    for (Object key : STREET_CODES.keySet()) {
      addRoadSuffixTerms((String)key);
    }
  }
  
  private static final Properties STREET_CODES = buildCodeTable(new String[]{
      "AL", "ALLEY",
      "AN", "ANNEX",
      "AR", "ARCADE",
      "AV", "AVE",
      "BC", "BEACH",
      "BG", "BRIDGE",
      "BL", "BLUFF",
      "BN", "BEND",
      "BP", "BYPASS",
      "BT", "BOTTOM",
      "BV", "BOULEVARD",
      "CC", "CRESCENT",
      "CE", "COURSE",
      "CI", "CIRCLE",
      "CO", "CORRIDOR",
      "CR", "CREST",
//      "CT", "COURT",
      "CX", "CROSSING",
//      "DR", "DRIVE",
//      "EO", "EAST OF",
      "EX", "EXPRESSWAY",
      "FA", "FALL",
      "FG", "FRONTAGE",
      "FK", "FORK",
      "FL", "FIELD",
      "FR", "FERRY",
      "FS", "FOREST",
      "FT", "FORT",
      "FY", "FREEWAY",
      "GD", "GARDENS",
      "GL", "GLEN",
      "GR", "GREEN",
      "GT", "GATEWAY",
      "HL", "HILL",
      "HO", "HOLLOW",
      "HT", "HEIGHTS",
      "HW", "HWY",
      "IL", "ISLE",
      "IN", "INLET",
      "IS", "ISLAND",
      "JC", "JUNCTION",
      "LC", "LOCKS",
      "LD", "LANDING",
      "LG", "LODGE",
      "LK", "LAKE",
      "LN", "LANE",
      "LP", "LOOP",
      "MA", "MALL",
      "MD", "MEADOWS",
      "ML", "MILE",
      "MN", "MOUNTAIN",
      "MP", "MILE POST",
      "MT", "MOUNTAIN",
      "MW", "MOLALLA WESTERN",
      "OR", "ORCHARD",
      "OV", "OVAL",
      "PA", "PATH",
      "PI", "PIKE",
      "PK", "PARKWAY",
//      "PL", "PLACE",
      "PO", "PORT",
      "PS", "PASS",
      "PT", "POINT",
      "PW", "PORTLAND WESTERN",
      "PZ", "PLAZA",
      "RA", "RAMP",
//      "RD", "ROAD",
      "RG", "RIDGE",
      "RI", "RIVER  ",
      "RM", "RIVER MILE",
      "RN", "RANCH",
      "RP", "RAPIDS",
      "RR", "RAILROAD",
      "RU", "RUN",
      "RW", "ROW",
      "SA", "STATION",
//      "SE", "STREAM",
      "SH", "SHORE",
      "SL", "SHOAL",
      "SQ", "SQUARE",
      "SR", "SHORE",
//      "ST", "STREET",
      "SU", "SPUR",
      "TA", "TERRACE",
      "TC", "TRACK",
      "TE", "TERRACE",
      "TK", "TURNPIKE",
      "TL", "TRAIL",
      "TR", "TRAIL",
      "TU", "TUNNEL",
      "UN", "UNION   ",
      "UP", "UNION PACIFIC",
      "VG", "VILLIAGE",
      "VI", "VIA",
      "VL", "VILLIAGE",
      "VS", "VISTA",
      "VW", "VIEW",
      "VY", "VALLEY",
      "WA", "WALK",
//      "WO", "WEST OF",
      "WY", "WAY"
  });
}
