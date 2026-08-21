package net.anei.cadpage.parsers.CA;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

/**
 * Sonoma County, CA
 */
public class CASonomaCountyParser extends FieldProgramParser {

  public CASonomaCountyParser() {
    super(CITY_CODES,
           "SONOMA COUNTY", "CA",
           "LOC:ADDRCITYST/S! ( BOX:BOX! CN:NAME! CLLR#:PHONE! TYPE:CALL! ALARM#:PRI! CALLER_ADDR:SKIP! DISP:UNIT! TIME:TIME! COM:INFO! INFO/N+ CASE:ID! " +
                             "| TYP:CALL! REMARKS:INFO! INFO/N+ CN:NAME! CALLER_ADDR:SKIP! PHONE:PHONE! EVENT__#:ID! " +
                             ") END",
           FLDPROG_DOUBLE_UNDERSCORE);
  }

  @Override
  public String getFilter() {
    return "ps-cst@sonoma-county.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private Set<String> unitSet = new HashSet<String>();

  private static final Pattern POST_MOVE_PTN = Pattern.compile("Unit (\\S*) Relocate to: @(?:(.*) - )?(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = POST_MOVE_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL UNIT PLACE ADDR");
      data.strCall = "Post Move";
      data.strUnit = match.group(1);
      data.strPlace = getOptGroup(match.group(2));
      parseAddress(match.group(3).trim(), data);
      return true;
    }
    unitSet.clear();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern LEAD_STN_PTN1 = Pattern.compile("([A-Z]{2,3}):(?: @([A-Z0-9]+) *:)? *(.*)");
  private static final Pattern LEAD_STN_PTN2 = Pattern.compile(".*? ([A-Z]{2,3}): @([A-Z0-9]+) *: *(.*)");
  private static final Pattern COLON_MARKER = Pattern.compile(" *: @ *");
  private static final Pattern TRAIL_ZIP_PTN = Pattern.compile("(.*?)(?: +\\d{5})+");
  private static final Pattern NOT_APT_PTN = Pattern.compile("[A-Z ]{3,}|CA");

  private class MyAddressCityStateField extends AddressCityStateField {

    @Override
    public void parse(String field, Data data) {

      String leadCity = null;
      Matcher match = LEAD_STN_PTN1.matcher(field);
      if (match.matches()) {
        leadCity = match.group(1);
        data.strUnit = match.group(2);
        field = match.group(3);
      }
      else if ((match = LEAD_STN_PTN2.matcher(field)).matches()) {
        leadCity =  match.group(1);
        data.strUnit = match.group(2);
        field = match.group(3);
      }

      String gps = null;
      if (field.startsWith("LL(")) {
        int pt = field.indexOf(")");
        if (pt < 0) abort();
        gps = field.substring(0,pt+1);
        field = field.substring(pt+1).trim();
        field = stripFieldStart(field, ": EST ");
      }

      // Split everything by colon markers
      // First part is address.  Everything else is a place name
      String[] parts = COLON_MARKER.split(field);
      field = parts[0];
      for (int ndx = 1; ndx < parts.length; ndx++) {
        String place = stripFieldStart(parts[ndx], "UNIT AT:");
        if (!data.strPlace.contains(place)) {
          data.strPlace = append(data.strPlace, "-", place);
        }
      }

      // Strip off trailing zip code(s)
      match = TRAIL_ZIP_PTN.matcher(field);
      if (match.matches()) field = match.group(1).trim();

      // Strip off trailing apt, making sure it doesn't look like a city or state
      String apt = "";
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        if (NOT_APT_PTN.matcher(apt).matches()) {
          apt = "";
        } else {
          field = field.substring(0,pt).trim();
        }
      }

      // Now parse whaever is left
      field = field.replace(" X ", " & ");
      super.parse(field, data);

      // Final cleanup
      data.strApt = append(data.strApt, "-", apt);
      if (leadCity != null && data.strCity.isEmpty()) {
        data.strCity = convertCodes(leadCity, CITY_CODES);
      }

      if (gps != null) {
        if (!data.strAddress.isEmpty()) data.strPlace = data.strAddress;
        data.strAddress = gps;
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST PLACE UNIT?";
    }
  }

  private static final Pattern MED_CODE_PTN = Pattern.compile("MED \\((\\S*)\\)");

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MED_CODE_PTN.matcher(field);
      if (match.matches()) {
        String code = match.group(1);
        String call = CALL_CODES.getCodeDescription(code);
        if (call != null) {
          data.strCode = code;
          data.strCall = call;
          return;
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable();

  private static final Pattern GPS_PTN = Pattern.compile("^(?:N )?(-\\d{3}.\\d{4,}) (?:T )?(\\d{2}.\\d{4,})(?: METERS(?: \\d+)?)?(?: +WPH\\d)? *");
  private static final Pattern JUNK_PTN = Pattern.compile(" *(?:Unit ([A-Z0-9]+) (?:requested case number [A-Z0-9]+|.*)|\\*\\* Case number (?:[A-Z0-9]+ has been assigned for [:A-Z0-9]+|.*)|\\*\\* >>>> (?:by: [A-Z ]+ on terminal: [a-z0-9]+|.*)) *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      Matcher match = GPS_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        field = field.substring(match.end());
      }

      match = JUNK_PTN.matcher(field);
      if (match.find()) {
        int  last = 0;
        String result = "";
        do {
          result = append(result, " - ", field.substring(last,match.start()));
          last = match.end();
          String unit = match.group(1);
          if (unit !=  null) addUnit(unit, data);
        } while (match.find());
        result = append(result, " - ", field.substring(last));
        field = result;
      }

      int pt = field.indexOf("**");
      if (pt >= 0) {
        String tail = field.substring(pt);
        if ("** Case number ".startsWith(tail) || "** >>>> ".startsWith(tail)) {
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS INFO UNIT";
    }
  }

  private void addUnit(String unit, Data data) {
    unit = unit.toUpperCase();
    if (unitSet.add(unit)) data.strUnit = append(data.strUnit, " ", unit);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AGU", "AQUA CALIENTE",
      "ANP", "ANNAPOLIS",
      "AST", "CLOVERDALE",
      "BBY", "BODEGA BAY",
      "BEL", "SANTA ROSA",
      "BEN", "SANTA ROSA",
      "BLO", "PETALUMA",
      "BOD", "BODEGA",
      "BOY", "BOYES HOT SPRINGS",
      "CAM", "CAMP MEEKER",
      "CAZ", "CAZADERO",
      "CL",  "CLOVERDALE",
      "CLO", "CLOVERDALE",
      "CO",  "COTATI",
      "COT", "COTATI",
      "DRC", "HEALDSBURG",
      "DUN", "DUNCANS MILLS",
      "ELD", "ELDRIDGE",
      "ELV", "EL VERANO",
      "FIT", "HEALDSBURG",
      "FOR", "FORESTVILLE",
      "FTR", "FORT ROSS",
      "FUL", "SANTA ROSA",
      "GEY", "GEYSERVILLE",
      "GLE", "GLEN ELLEN",
      "GNVL","GUERNEVILLE",
      "GRA", "GRATON",
      "GUE", "GUERNVILLE",
      "HBG", "HEALDSBURG",
      "HE",  "HEALDSBURG",
      "HEA", "HEALDSBURG",
      "HES", "SEBASTOPOL",
      "JCD", "JCD",
      "JEN", "JENNER",
      "KEN", "KENWOOD",
      "KNI", "CALISTOGA",
      "KOR", "FORESTVILLE",
      "LAK", "PETALUMA",
      "LAR", "SANTA ROSA",
      "LKC", "LAKE COUNTY",
      "LSO", "GEYSERVILLE",
      "MAY", "GLEN ELLEN",
      "MEN", "MENDOCINO COUNTY",
      "MRN", "MARIN COUNTY",
      "MRO", "MONTE RIO",
      "MTN", "CALISTOGA",
      "MWS", "SANTA ROSA",
      "NAP", "NAPA",
      "NOV", "NOVATO",
      "OCC", "OCCIDENTAL",
      "PE",  "PETALUMA",
      "PEN", "PENNGROVE",
      "PET", "PETALUMA",
      "RIN", "SANTA ROSA",
      "RLN", "HEALDSBURG",
      "RND", "RIO NIDO",
      "ROH", "ROHNERT PARK",
      "ROS", "SANTA ROSA",
      "RP",  "ROHNERT PARK",
      "RS",  "ROSELAND",
      "SCH", "SCHELLVILLE",
      "SE",  "SEBATOPOL",
      "SEB", "SEBATOPOL",
      "SNMA","SONOMA",
      "SO",  "SONOMA",
      "SOL", "SOLANO COUNTY",
      "SON", "SONOMA",
      "SR",  "SANTA ROSA",
      "SRO", "SANTA ROSA",
      "SSU", "ROHNERT PARK",
      "TCG", "PETALUMA",
      "TIM", "TIMBER COVE",
      "TSR", "SEA RANCH",
      "TWI", "SEBATOPOL",
      "TWR", "PETALUMA",
      "VFR", "VALLEY FORD",
      "VJO", "VALLEJO",
      "WI",  "WINDSOR",
      "WIN", "WINDSOR",
      "WSR", "SANTA ROSA",

      "ANCHOR BAY",   "ANCHOR BAY",
      "CLSTGA",       "CALISTOGA",
      "GEYSERVILLE",  "GEYSERVILLE",
      "GUALALA",      "GUALALA",
      "MANCHESTER",   "MANCHESTER",
      "POINT ARENA",  "POINT ARENA",
      "PT ARENA",     "PT ARENA",
      "PT AREANA",    "PT ARENA",
      "SEA RANCH",    "SEA RANCH",
      "SBSTPL",       "SEBATOPOL",
      "TOMALAES",     "TOMALES",
      "TOMALES",      "TOMALES",

      "BDGA",      "BODEGA",
      "BDGA BAY",  "BODEGA BAY"
  });
}
