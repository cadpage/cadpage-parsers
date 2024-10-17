package net.anei.cadpage.parsers.MN;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * This class parses messages from some as yet unidentified CAD software vendor
 **/

/**
 * Hennepin county, MN
 */
public class MNHennepinCountyAParser extends FieldProgramParser {

  public MNHennepinCountyAParser() {
    super(CITY_LIST, "HENNEPIN COUNTY", "MN",
          "( NAME:NAME! ( LOC:ADDR! CITY:CITY? EVTYPE:CALL! " +
                       "| PH#:PHONE! ( LOC_NAME:PLACE | PREM_NAME:PLACE ) ADDRESS:ADDR! APT#:APT! CITY:CITY! ( X_ST:X | XSTREET:X ) EVTYPE:CALL! INC#:ID! " +
                       "| ADDRESS:ADDR! APT#:APT! CITY:CITY! XSTREET:X! EVTYPE:CALL! INC#:ID! " +
                       ") COMMENTS:INFO/N+ UNITS:UNIT END" +
          "| INC#:ID! ADDRESS:ADDR! EVTYPE:CALL! INFO/RN+ " +
          ")");
    setBreakChar('-');
  }

  @Override
  public String getFilter() {
    return "sheriff.cadpaging@co.hennepin.mn.us,SHERIFF.CADPAGING@HENNEPIN.US,osseofiredepartment@googlegroups.com,firepaging@mapleplain.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    int pt = body.indexOf("\n\n\n");
    if (pt < 0) pt = body.indexOf("\n\n--");
    if (pt >= 0) body = body.substring(0, pt).trim();

    body = body.replace(" X STREET-", " XSTREET-").replace(" ADDRSS-", " ADDRESS-").replace("; UNITS-", "\n UNITS-");
    if (!super.parseFields(splitFields(body), data)) return false;

    // If we did not find a city, check the name to see if it looks like
    // a neighboring county dispatch center
    if (data.strCity.length() == 0) {
      Matcher match = COUNTY_NAME_PTN.matcher(data.strName);
      if (match.find()) data.strCity = match.group().toUpperCase() + " COUNTY";
    }

    return true;
  }
  private static final Pattern COUNTY_NAME_PTN = Pattern.compile("\\b(ANOKA|RAMSEY|DAKOTA|SCOTT|CARVER|WRIGHT)\\b", Pattern.CASE_INSENSITIVE);

  /**
   * Break message body into list of fields
   * @param body message body
   * @return array of fields
   */
  private static String[] splitFields(String body) {

    // FIgure out which delimiter is being used
    int colonPt = body.indexOf(':');
    int semiPt = body.indexOf(';');
    if (colonPt < 0) colonPt = 1000;
    if (semiPt < 0) semiPt = 1000;
    char delim = colonPt < semiPt ? ':' : ';';
    List<String> fields = new ArrayList<>();
    int last = 0;
    boolean semiBreak = true;
    for (int pt = 0; pt < body.length(); pt++) {
      char chr = body.charAt(pt);
      if (chr == delim && semiBreak || chr == '\n') {
        fields.add(body.substring(last, pt).trim());
        last = pt+1;

        if (semiBreak) {
          String part = body.substring(last).trim();
          if (part.startsWith("COMMENTS-")) semiBreak = false;
        }
      }
    }
    fields.add(body.substring(last).trim());
    return fields.toArray(new String[0]);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "@");
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)(?:,) *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      // Break off things from end separated by [
      String apt = "";
      String extra = "";
      Parser p = new Parser(field);
      while (true) {
        String part = p.getLastOptional('[');
        if (part.length() == 0) break;

        // For each chunk try to split off a city name
        part = splitCity(part, data);

        // Now we get to sort out what each part is.
        // Apartment?
        if (part.startsWith("#")) {
          apt = append(part.substring(1).trim(), "-", apt);
        }

        // Place name
        else if (part.startsWith("@")) {
          data.strPlace = append(part.substring(1).trim(), " - ", data.strPlace);
        }

        // City name or code
        else if (part.startsWith("=")) {
          data.strCity = convertCodes(part.substring(1).trim(), CITY_CODES);
        }

        // City name
        else if (isCity(part)) {
          data.strCity = part;
        }

        // City code
        else {
          String city = CITY_CODES.getProperty(part);
          if (city != null) {
            data.strCity = city;
          }

          // Anything else turns into extra address information
          else {
            part = part.replace("(", "").replace(")", "").trim();
            part = part.replaceAll("  +", " ");
            extra = append(part, " / ", extra);
          }
        }
      }

      apt = append(p.getLastOptional(" APT "), "-", apt);

      field = p.get();
      field = splitCity(field, data);
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);

      data.strApt = append(data.strApt, "-", apt);

      if (extra.length() > 0) {
        data.strAddress = append(data.strAddress, " ", '(' + extra + ')');
      }
    }

    private String splitCity(String part, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(part);
      if (match.matches()) {
        data.strCity = match.group(2);
        return match.group(1).trim();
      }
      int pt = part.lastIndexOf('-');
      if (pt >= 0) {
        String city = part.substring(pt+1).trim();
        if (isCity(city)) {
          data.strCity = city;
          return part.substring(0,pt).trim();
        }
      }
      return part;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strCity = convertCodes(field, CITY_CODES);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "FD ");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_PFX_PTN = Pattern.compile("\\[\\d+\\] *(.*)");
  private static final Pattern INFO_CROSS_PTN = Pattern.compile(".*: ?cross streets ?: *(.*)");
  private static final Pattern INFO_UNIT_PTN = Pattern.compile("Resources required for this event are: *(.*?);?");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d+ Nearby Events found .*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String connect = "\n";
      Matcher match = INFO_PFX_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      for (String part : field.trim().split("  +")) {
        match = INFO_CROSS_PTN.matcher(part);
        if (match.matches()) {
          data.strCross = match.group(1).trim();
          continue;
        }
        match = INFO_UNIT_PTN.matcher(part);
        if (match.matches()) {
          data.strUnit = append(data.strUnit, " ", match.group(1));
          continue;
        }
        if (INFO_JUNK_PTN.matcher(part).matches()) continue;
        if (data.strAddress.length() > 0) {
          if (data.strAddress.startsWith(part)) continue;
          if (part.startsWith(data.strAddress)) continue;
        }
        if (part.length() > 0) {
          data.strSupp = append(data.strSupp, connect, part);
          connect = "  ";
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "X INFO UNIT";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANC", "ANOKA",
      "ANO", "ANOKA COUNTY",
      "BKC", "BROOKLYN CENTER",
      "BKP", "BROOKLYN PARK",
      "BLM", "BLOOMINGTON",
      "CAR", "CARVER COUNTY",
      "CHP", "CHAMPLIN",
      "COR", "CORCORAN",
      "CRY", "CRYSTAL",
      "DAK", "DAKOTA COUNTY",
      "DAY", "DAYTON",
      "DPH", "DEEPHAVEN",
      "EDN", "EDINA",
      "EDP", "EDEN PRAIRIE",
      "EXC", "EXCELSIOR",
      "FTS", "FORT SNELLING",
      "GDV", "GOLDEN VALLEY",
      "GRF", "GREENFIELD",
      "GRW", "GREENWOOD",
      "HAN", "HANOVER",
      "HOP", "HOPKINS",
      "IND", "INDEPENDENCE",
      "LLK", "LONG LAKE",
      "LOR", "LORETTO",
      "MAP", "METROPOLITAN AIRPORT",
      "MED", "MEDINA",
      "MLK", "MEDICINE LAKE",
      "MND", "MOUND",
      "MPG", "MAPLE GROVE",
      "MPL", "MINNEAPOLIS",
      "MPP", "MAPLE PLAIN",
      "MTB", "MINNETONKA BEACH",
      "MTK", "MINNETONKA",
      "MTT", "MINNETRISTA",
      "NHP", "NEW HOPE",
      "ORO", "ORONO",
      "OSE", "OSSEO",
      "OTS", "OTSEGO",
      "PLY", "PLYMOUTH",
      "RAM", "RAMSEY COUNTY",
      "RCH", "RICHFIELD",
      "ROB", "ROBBINSDALE",
      "ROC", "ROCKFORD",
      "ROG", "HASSAN",
      "SCO", "SCOTT COUNTY",
      "SPK", "SPRING PARK",
      "STA", "ST ANTHONY",
      "STB", "ST BONIFACIUS",
      "STL", "ST LOUIS PARK",
      "SWD", "SHOREWOOD",
      "TKB", "TONKA BAY",
      "WAY", "WAYZATA",
      "WDL", "WOODLAND",
      "WRI", "WRIGHT COUNTY"

  });

  private static final String[] CITY_LIST = new String[]{

    // Anakoka County
    "COLUMBIA HEIGHTS",
    "FRIDLEY",

    // Carver County
    "WACONIA",
    "WATERTOWN"
  };
}