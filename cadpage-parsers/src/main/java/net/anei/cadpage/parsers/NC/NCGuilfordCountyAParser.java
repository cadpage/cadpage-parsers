package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Guilford County, NC
 */
public class NCGuilfordCountyAParser extends DispatchOSSIParser {

  private static final Pattern MARKER = Pattern.compile("^(?:(?:\\d{1,4}:)?[\\w@\\-\\.]+? *[\n:])?(?=CAD:|[A-Z]{3,4};)");

  public NCGuilfordCountyAParser() {
    this("GUILFORD COUNTY", "NC");
  }

  protected NCGuilfordCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "FYI? ID? ( CALL PRI ADDR EXTRA? X/Z+? UNIT INFO/N+ | ( CALL2 ADDR! | PRI/Z MUTUAL ADDR! | ( SRC SRC PRI | PRI? ) CODE? CALL ADDR! ) CODE? CITY? ( PLACE ID | ID? ) EXTRA? ( X X? | PLACE X X? | ) CODE? CITY? ( PRI UNIT? SRC SRC | ) XINFO+? UNIT CITY? Radio_Channel:CH? XINFO+? GPS1 GPS2 )");
  }

  @Override
  public String getFilter() {
    return "@edispatches.com,93001,firedistrict13@listserve.com,CAD@greensboro-nc.gov,mhfd38all@listserve.com,@summerfieldfire.com,CAD@highpointnc.gov";
  }

  @Override
  public String getAliasCode() {
    return "NCGilfordCounty";
  }

  private static final Pattern YADKIN_MSG_PTN = Pattern.compile(";[ A-Z0-9]+ X [ A-Z0-9]+;|;geo:| OCA:");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Reject anything that looks like it is a Southern based format for Yadkin County
    if (YADKIN_MSG_PTN.matcher(body).find()) return false;

    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end()).trim();
      if (!body.startsWith("CAD:")) body = "CAD:" + body;
    }

    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = body.replace("\\40", " ");
    if (!super.parseMsg(body, data)) return false;

    // More logic to reject NCRowanCounty calls
    if (data.strCallId.startsWith("704") ||
        data.strCallId.startsWith("864")) return false;

    // If out of county mutual aid call, cancel the default county
    if (data.strSource.startsWith("ALCO")) data.strCity = "ALAMANCE COUNTY";
    else if (data.strCall.equals("MUTUAL")) data.defCity = "";
    pt = data.strCity.indexOf(" / ");
    if (pt >= 0) data.strCity = data.strCity.substring(0,pt);

    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " CITY";
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{7,}", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("MUTUAL")) return new MutualField();
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("EXTRA")) return new ExtraField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("XINFO")) return new CrossInfoField();
    if (name.equals("UNIT")) return new UnitField("(?!OPS)(?:[A-Z]+\\d+|[A-Z]{1,3}FD)(?:,(?:[A-Z]+\\d+|[A-Z]{1,3}FD))*", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private static final Pattern BAD_CALL_PTN = Pattern.compile("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?+ .*|FYI:|Update:");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      // Reject anything that looks like a NCRowanCounty page
      if (BAD_CALL_PTN.matcher(field).matches()) abort();
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_CALL_PTN = Pattern.compile("\\{(\\S+)\\} *(.*)");
  private class MyCall2Field extends MyCallField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = match.group(1);
        data.strCall = match.group(2);
        return true;
      }

      if (field.length() >= 6) {
        data.strCall = field;
        return true;
      }

      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }

  private class MutualField extends CallField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.equals("MUTUAL")) return false;
      parse(field, data);
      return true;
    }
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      data.strSource = append(data.strSource, "-", field);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = field.toUpperCase();
      String city = CITY_CODES.getProperty(field);
      if (city != null) {
        data.strCity = city;
        return true;
      }

      if (CITY_SET.contains(field)) {
        data.strCity = field;
        return true;
      }

      return false;
    }
  }

  private static final Pattern CODE_PTN = Pattern.compile("\\d\\d[A-Z]\\d\\d[A-Za-z]?");
  private class MyCodeField extends CodeField {
    public MyCodeField() {
      setPattern(CODE_PTN, true);
    }
  }

  // Extra information field
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE|LOT)[: ] *(.*)");
  private static final Pattern SRC_PTN = Pattern.compile("[A-Z]{4}");
  private class ExtraField extends SkipField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("DIST:")) {
        data.strSupp = field;
        return true;
      }
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        return true;
      }
      if (NUMERIC.matcher(field).matches()) {

        // See if this might be a priority field that will be followed
        // by two source fields.
        if (field.length() > 1 || !SRC_PTN.matcher(getRelativeField(+2)).matches()) {
          data.strApt = append(data.strApt, "-", field);
          return true;
        }
      }
      return false;
    }

    @Override
    public String getFieldNames() {
      return "INFO APT";
    }
  }

  private static final Pattern X_BAD_PTN = Pattern.compile("(?:REQUESTING|NEEDS)\\b.*");
  private static final Pattern X_GOOD_PTN = Pattern.compile("AVENUE +\\S+");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (X_BAD_PTN.matcher(field).matches()) return false;
      if (super.checkParse(field, data)) return true;
      if (X_GOOD_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return true;
      }
      return false;
    }
  }

  private static final Pattern INFO_CH_PTN = Pattern.compile("\\d{1,2}");
  private class CrossInfoField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, " - ", match.group(1));
        return;
      }

      // If we stumble across a Rowan County city code, abort
      if (BAD_INFO_PTN.matcher(field).matches()) abort();
      if (BAD_CITY_CODES.contains(field)) abort();

      String city = CITY_CODES.getProperty(field);
      if (city != null) {
        data.strCity = city;
      } else if (CITY_SET.contains(field)) {
        data.strCity = field;
      } else if (field.startsWith("TAC ") || field.startsWith("tac ")) {
        data.strChannel = field;
      } else if (INFO_CH_PTN.matcher(field).matches()) {
        data.strChannel = "TAC " + field;
      } else if (data.strCode.length() == 0 && CODE_PTN.matcher(field).matches()) {
        data.strCode = field;
      } else if (data.strCall.length() == 0) {
        data.strCall = field;
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY INFO CODE CH CALL";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");
  private static final Pattern GPS_TRUNC_PTN = Pattern.compile("[-+]?[\\.\\d]+|[-+]");
  private class MyGPSField extends GPSField {

    private int type;

    public MyGPSField(int type) {
      super(type);
      this.type = type;
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField(+2)) return false;
      if (GPS_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return true;
      }

      if (!GPS_TRUNC_PTN.matcher(field).matches()) return false;
      if (type == 1 && !field.startsWith("3")) return false;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  public static boolean isCityCode(String code) {
    return CITY_CODES.getProperty(code) != null;
  }

  // Token pattern that only occur in Rowan County calls, which also should force an automatic rejectd
  private static final Pattern BAD_INFO_PTN = Pattern.compile("OPS\\d{1,2}");

  // These are the city codes used by the Rowan County, NC location parser
  // Finding any of them is grounds to reject this text message
  private static final Set<String> BAD_CITY_CODES = new HashSet<String>(Arrays.asList(new String[]{
      "CHGV",
      "CLVD",
      "ESPN",
      "FATH",
      "GOLD",
      "GRQY",
      "KANN",
      "LAND",
      "MOOR",
      "ROCK",
      "SALS",
      "SPEN",
      "WOOD"
  }));

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "A",    "ARCHDALE",
      "ALAM", "ALAMANCE COUNTY",
      "ARC",  "ARCHDALE",
      "B",    "BROWN SUMMIT",
      "BUR",  "BURLINGTON",
      "CL",   "CLEMMONS",
      "CLI",  "CLIMAX",
      "COL",  "COLFAX",
      "DAVI", "DAVIDSON COUNTY",
      "DEN",  "DENTON",
      "E",    "ELON",
      "EDEN", "EDEN",
      "FORS", "FORSYTH COUNTY",
      "G",    "GREENSBORO",
      "GI",   "GIBSONVILLE",
      "GREE", "GREENSBORO",
      "GUIL", "GUILFORD COUNTY",
      "H",    "HIGH POINT",
      "HP",   "HIGH POINT",
      "J",    "JAMESTOWN",
      "JU",   "JULIAN",
      "K",    "KERNERSVILLE",
      "KV",   "KERNERSVILLE",
      "L",    "LIBERTY",
      "LEX",  "LEXINGTON",
      "LIN",  "LINWOOD",
      "M",    "MCLEANSVILLE",
      "NL",   "NEW LONDON",
      "OAK",  "OAK RIDGE",
      "P",    "PLEASANT GARDEN",
      "RAN",  "RANDLEMAN",
      "RAND", "RANDOLPH COUNTY",
      "REI",  "REIDSVILLE",
      "ROCK", "ROCKINGHAM COUNTY",
      "RWC",  "ROWAN COUNTY",
      "SE",   "SEDALIA",
      "ST",   "STOKESDALE",
      "SU",   "SUMMERFIELD",
      "T",    "THOMASVILLE",
      "THA",  "THOMASVILLE",
      "TROY", "TROY",
      "W",    "WHITSETT",
      "WELC", "WELCOME",
      "WS",   "WINSTON SALEM",
      "X",    "UNIDENTIFIED",
  });

  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(

      // Cities
      "ARCHDALE",
      "BURLINGTON",
      "GREENSBORO",
      "HIGH POINT",

      // Towns
      "GIBSONVILLE",
      "JAMESTOWN",
      "KERNERSVILLE",
      "OAK RIDGE",
      "PLEASANT GARDEN",
      "SEDALIA",
      "STOKESDALE",
      "SUMMERFIELD",
      "WHITSETT",

      // Townships
      "BRUCE",
      "CENTER GROVE",
      "CLAY",
      "DEEP RIVER",
      "FENTRESS",
      "FRIENDSHIP",
      "GILMER",
      "GREENE",
      "HIGH POINT",
      "JAMESTOWN",
      "JEFFERSON",
      "MADISON",
      "MONROE",
      "MOREHEAD",
      "OAK RIDGE",
      "ROCK CREEK",
      "SUMNER",
      "WASHINGTON",

      // Census-designated places
      "FOREST OAKS",
      "JULIAN",
      "MCLEANSVILLE",

      // Unincorporated communities
      "BROWN SUMMIT",
      "BROWNS SUMMIT",
      "CLIMAX",
      "COLFAX",
      "MONTICELLO",

      // Alamance County
      "ELON",

      // Davidson County
      "THOMASVILLE",

      // Forsyth County
      "BELEWS CREEK",
      "BELEWS CREEK / FORSY",
      "KERNERSVILLE",


      // Guilford County
      "SEDALIA",

      // Randolph County
      "LIBERTY",
      "RANDLEMAN",

      // Rockingham County
      "EDEN",
      "REIDSVILLE",

      // Counties
      "ALAMANCE COUNTY",
      "DAVIDSON COUNTY",
      "FORSYTH COUNTY",
      "GUILFORD COUNTY",
      "RANDOLPH COUNTY",
      "ROCKINGHAM COUNTY"
  ));

}
