package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Washington County, MD
 */
public class MDWashingtonCountyParser extends FieldProgramParser {

  public MDWashingtonCountyParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "MD",
        "( INFO/G END " +
        "| ADDR/SXP CITY? ( PLACE X | X | ) CALL! CALL+? ( TRAIL1% END | UNIT UNIT+? ( TRAIL2% END | INFO1+? TRAIL3% END ) ) " +
        ")");
    addExtendedDirections();
    removeWords("CV");
  }

  @Override
  public String getFilter() {
    return "Dispatch@washco-md.net,cadnotifications@washco-md.net,@c-msg.net,TextAlert@sems79.org,@alert.active911.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean noParseSubjectFollow() { return true; }
    };
  }

  private static final Pattern CALL_QUAL_PTN = Pattern.compile("(?:Recall Reason|Completed|Cancel Reason):.*\n|(?:CANCEL|CANCELL?ED|CALL CANCELL?ED|FAILED!).*\n|[- A-Za-z0-9!\\.\\*',]+\n");
  private static final Pattern CROSS_PTN = Pattern.compile("\\[([^\\[\\]]*) - ([^\\[\\]]*)\\]");
  private static final Pattern DELIM = Pattern.compile(" *(?<= )- +|  ,");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // Look for call qualifier prefix
    Matcher match = CALL_QUAL_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCall = match.group().trim();
      body = body.substring(match.end()).trim();
    }

    // Drop everything after the first newline
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();

    pt = body.indexOf(" / [!] ");
    if (pt >= 0) {
      subject = body.substring(0,pt).trim();
      body = body.substring(pt+7).trim();
    }

    if (subject.endsWith("|!")) subject = subject.substring(0,subject.length()-2).trim();
    if (!subject.equals("CAD") && !subject.equals("!") && !subject.equals("Dispatch")) data.strSource = subject;

    // Standard cross street field contains a spurious delimiter that we need to protect
    body = CROSS_PTN.matcher(body).replaceFirst("[$1 & $2]");

    // Split body into fields separated by  -
    if (!parseFields(DELIM.split(body), data)) return false;

    if (data.msgType == MsgType.GEN_ALERT && !isPositiveId()) return false;

    data.strCity = convertCodes(data.strCity, MISSPELLED_CITIES);
    String state = CITY_STATE_TABLE.getProperty(data.strCity.toUpperCase());
    if (state != null) data.strState = state;
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    if (name.equals("TRAIL1")) return new TrailField(1);
    if (name.equals("TRAIL2")) return new TrailField(2);
    if (name.equals("TRAIL3")) return new TrailField(3);
    return super.getField(name);
  }

  private static final Pattern BOX_PTN = Pattern.compile("(.*)\\bBOX +([^ ]+(?: [A-Z])?)");
  private static final Pattern MA_COUNTY_PTN = Pattern.compile("([A-Z]{3,4}) CO, *(.*)");
  private static final Pattern DIR_BOUND_PTN = Pattern.compile("([NSEW]B)\\b *(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|RM|SUITE|LOT)\\b *(.*)");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {

      // Kill off partial results
      if (field.contains("ProQA")) abort();

      // Strip trailing box
      Matcher match = BOX_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strBox = match.group(2);
      }

      // First field contains option M/A county, address and optional place name
      String maCounty = "";
      match = MA_COUNTY_PTN.matcher(field);
      if (match.matches()) {
        maCounty = convertCodes(match.group(1), COUNTY_CODES);
        field = match.group(2);
      }

      Parser p = new Parser(field);
      super.parse(p.get(','), data);
      String place = p.get();
      p = new Parser(place);
      String city = p.getLast(',');
      if (isCity(city)) {
        data.strCity = city;
        place = p.get();
      }
      match = DIR_BOUND_PTN.matcher(place);
      if (match.matches()) {
        data.strAddress = append(data.strAddress, " ", match.group(1));
        place = match.group(2);
      }
      match = APT_PTN.matcher(place);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
      } else {
        data.strPlace = append(data.strPlace, " - ", place);
      }
      if (data.strCity.length() == 0) data.strCity = maCounty;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY BOX";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("[") || ! field.endsWith("]")) return false;
      field = field.substring(1, field.length()-1).trim();
      if (field.endsWith("&")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
      return true;
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {

      // Kill off partial results
      if (field.contains("ProQA")) abort();

      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("(?:[0-9]?[A-Z]+[0-9]+[A-Z]?|[0-9]{4}|[A-Z]{2})(?:[,\\$][A-Z0-9,\\$]+)?\\b");
  private class MyUnitField extends UnitField {
    public MyUnitField() {
      setPattern(UNIT_PTN, true);
    }
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, ",", field);
    }
  }

  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp," - ", field);
    }
  }

  // TrailField always handles the last field in the text string.  It always has a date field
  //  and may have an ID field.  Depending on the context, there are three possibilities for
  // a leading unit field
  //   TRAIL1 - Leading UNIT field is required
  //   TRAIL2 - Leading UNIT field is optional
  //   TRAIL3 - Leading UNIT field will not be present

  private static final Pattern TIME_PTN = Pattern.compile("(?<=^| )(\\d\\d:\\d\\d)\\b");
  private static final Pattern TIME2_PTN = Pattern.compile("\\b[\\d:]*$");
  private static final Pattern ID_PTN = Pattern.compile("\\b(?:[EF]\\d{9}(?: \\d{7})?|\\d{7})$");
  private static final Pattern TG_PTN = Pattern.compile("\\bTG: *(\\S+)(?: (.*))?$");
  private class TrailField extends Field {

    private int type;

    public TrailField(int type) {
      this.type = type;
    }

    @Override
    public void parse(String field, Data data) {

      // Check for leading unit field
      if (type <= 2) {
        Matcher match = UNIT_PTN.matcher(field);
        if (match.lookingAt()) {
          data.strUnit = append(data.strUnit, ",", match.group());
          field = field.substring(match.end()).trim();
        } else if (type == 1) abort();
      }

      String sPart1, sPart2;
      Matcher match = TIME_PTN.matcher(field);
      if (match.find()) {
        data.strTime = match.group(1);
        sPart1 = field.substring(0,match.start()).trim();
        sPart2 = field.substring(match.end()).trim();
      } else {
        data.expectMore = true;
        sPart1 = field;
        sPart2 = "";
        match = TIME2_PTN.matcher(field);
        if (match.find()) sPart1 = field.substring(0,match.start()).trim();
      }

      match = TG_PTN.matcher(sPart1);
      if (match.find()) {
        data.strChannel = match.group(1);
        data.strCallId = getOptGroup(match.group(2));
        sPart1 = sPart1.substring(0,match.start()).trim();
      } else {
        match = ID_PTN.matcher(sPart1);
        if (match.find()) {
          data.strCallId = match.group();
          sPart1 = sPart1.substring(0,match.start()).trim();
        }
      }

      data.strSupp = append(data.strSupp, " - ", sPart1);
      data.strSupp = append(data.strSupp, " - ", sPart2);
    }

    @Override
    public String getFieldNames() {
      return "UNIT INFO CH ID TIME";
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = LONG_DIR_PTN.matcher(addr);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        match.appendReplacement(sb, match.group().substring(0,1));
      } while (match.find());
      match.appendTail(sb);
      addr = sb.toString();
    }
    return super.adjustMapAddress(addr);
  }
  private static final  Pattern LONG_DIR_PTN = Pattern.compile("\\b(NORTH|SOUTH|EAST|WEST)\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("ROHRERSVILLE")) city = "";
    return city;
  }

  private static final String[] CITY_LIST = new String[]{

    // City
    "HAGERSTOWN",

    // Towns
    "BOONSBORO",
    "CLEAR SPRING",
    "FUNKSTOWN",
    "HANCOCK",
    "KEEDYSVILLE",
    "SHARPSBURG",
    "SMITHSBURG",
    "WILLIAMSPORT",

    // Census-designated places
    "CASCADE",
    "CAVETOWN",
    "CHEWSVILLE",
    "FORT RITCHIE",
    "FOUNTAINHEAD-ORCHARD HILLS",
    "HALFWAY",
    "HIGHFIELD",
    "HIGHFIELD-CASCADE",
    "LEITERSBURG",
    "LONG MEADOW",
    "LONGMEADOW",
    "MAUGANSVILLE",
    "MOUNT AETNA",
    "MOUNT LENA",
    "ORCARD HILLS",
    "PARAMOUNT-LONG MEADOW",
    "ROBINWOOD",
    "ROHRERSVILLE",
    "SAINT JAMES",
    "SAN MAR",
    "WILSON-CONOCOCHEAGUE",

    // Unincorporated communities
    "ANTIETAM",
    "BEAVER CREEK",
    "BENEVOLA",
    "BIG POOL",
    "BROADFORDING",
    "BROWNSVILLE",
    "BURTNER",
    "CEARFOSS",
    "CEDAR GROVE",
    "DARGAN",
    "DOWNSVILLE",
    "EAKLES MILLS",
    "FAIRPLAY",
    "FAIRVIEW",
    "GAPLAND",
    "HUYETT",
    "INDIAN SPRINGS",
    "JUGTOWN",
    "MAPLEVILLE",
    "MERCERSVILLE",
    "PECKTONVILLE",
    "PEN MAR",
    "PINESBURG",
    "RINGGOLD",
    "SAMPLES MANOR",
    "SANDY HOOK",
    "SPIELMAN",
    "TREGO",
    "VAN LEAR",
    "WEVERTON",
    "WOODMONT",

    // Franklin County
    "GREENCASTLE",
    "THURMONT",
    "WASH TWP",
    "WASHINGTON TWNSP",
    "WASHINGTON TWP",
    "WAYNESBORO",

    // Jefferson County
    "SHEPHERDSTOWN"
  };

  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "ALL",  "ALLEGANY COUNTY",
      "BER",  "BERKELEY COUNTY",
      "FRA",  "FRANKLIN COUNTY",
      "FRE",  "FREDERICK COUNTY",
      "FUL",  "FULTON COUNTY",
      "JEF",  "JEFFERSON COUNTY",
      "LOUD", "LOUDOUN COUNTY",
      "MOR",  "MORGAN COUNTY"
  });

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "WASH TWP",         "WASHINGTON TWP",
      "WASHINGTON TWNSP", "WASHINGTON TWP"
  });

  private static final Properties CITY_STATE_TABLE = buildCodeTable(new String[]{

      "GREENCASTLE",      "PA",
      "FRANKLIN COUNTY",  "PA",
      "FULTON COUNTY",    "PA",
      "WASHINGTON TWP",   "PA",
      "WAYNESBORO",       "PA",

      "LOUDOUN COUNTY",   "VA",

      "BERKELEY COUNTY",  "WV",
      "JEFFERSON COUNTY", "WV",
      "MORGAN COUNTY",    "WV",
      "SHEPHERDSTOWN",    "WV"
  });
}
