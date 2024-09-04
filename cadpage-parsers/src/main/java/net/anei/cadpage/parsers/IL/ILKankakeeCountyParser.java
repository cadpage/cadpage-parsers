package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILKankakeeCountyParser extends DispatchH05Parser {

  public ILKankakeeCountyParser() {
    super(CITY_LIST, "KANKAKEE COUNTY", "IL",
        "( SELECT/1 Call_Time:DATETIMEID! Call_Type:CALL! Address:ADDRCITY/S6! Additional_Location_Info:PLACE! Primary_Incident:SKIP! " +
        "| FULL END " +
        "| HEADER! INFO_BLK/Z+? TRAILER! " +
        ") END");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSaintNames("MARYS", "MARTINS", "PETERS");
    setupDoctorNames("JOHN");
  }

  @Override
  public String getFilter() {
    return "noreply@k3county.net,booking@k3county.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    // Check for message signature
    if (subject.equals("NWS Message")) {
      if (!parseMsg(body, data)) return false;

    } else if (body.startsWith("<style ")){
      setSelectValue("3");
      if (!super.parseHtmlMsg(subject, body, data)) return false;
    } else {
      return false;
    }

    String state = CITY_STATE_TABLE.getProperty(data.strCity);
    if (state != null) data.strState = state;
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) (\\d\\d?/\\d\\d?/\\d{4} .*)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (body.startsWith("Call Time:")) {
      setSelectValue("1");
      return parseFields(body.split("\n"), data);
    }

    setFieldList(FULL_FLD_LIST);
    return parseFull(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIMEID")) return new MyDateTimeIdField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("FULL")) return new MyFullField();
    if (name.equals("HEADER")) return new MyHeaderField();
    if (name.equals("TRAILER")) return new MyTrailerField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_ID_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)(\\d{4}-\\d{8}\\b.*)");
  private class MyDateTimeIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCallId = match.group(3).replace("(", "").replace(")", "");
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME ID";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      field = p.get("City:");
      String city = p.get();
      field = field.replace('@', '&');
      super.parse(field, data);
      if (data.strCity.isEmpty()) data.strCity = city;
    }
  }

  private class MyFullField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (!parseFull(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return FULL_FLD_LIST;
    }
  }

  private static final String FULL_FLD_LIST = "CALL ADDR APT CITY PLACE DATE TIME ID";

  private boolean parseFull(String field, Data data) {
    Matcher match = MASTER.matcher(field);
    if (!match.matches()) return false;
    if (!parseHeader(match.group(1), data)) return false;
    if (!parseTrailer(match.group(2), data)) return false;
    return true;
  }

  private class MyHeaderField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (!parseHeader(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT CITY PLACE";
    }
  }

  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=[a-z])(?=[A-Z0-9])");

  private boolean parseHeader(String field, Data data) {

    int pt = field.indexOf(',');
    if (pt >= 0) {
      String part1 = field.substring(0, pt).trim().replace('@', '&');
      String part2 = field.substring(pt+1).trim().replace(".", "");
      parseAddress(StartType.START_CALL, FLAG_RECHECK_APT | FLAG_ANCHOR_END, part1, data);
      boolean fixed = false;
      if (!data.strApt.isEmpty()) {
        char chr = data.strApt.charAt(0);
        if (chr >= 'a' && chr <= 'z') {
          pt = part2.indexOf(data.strApt);
          if (pt >= 0) {
            fixed = true;
            part2 = part2.substring(0,pt)+' '+part2.substring(pt);
          }
        }
      }
      if (!fixed) {
        part2 = MISSING_BLANK_PTN.matcher(part2).replaceAll(" ");
      }
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, part2, data);
    } else {
      field = field.replace('@', '&');
      parseAddress(StartType.START_CALL, field, data);
    }
    data.strPlace = getLeft();
    if (!data.strApt.isEmpty()) data.strPlace = stripFieldStart(data.strPlace, data.strApt);
    return true;
  }

  private class MyTrailerField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return parseTrailer(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME ID";
    }
  }

  private static final Pattern TRAILER_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) \\[?(\\d{4}-\\d{8}\\b.*?)\\]?");

  private boolean parseTrailer(String field, Data data) {
    Matcher match = TRAILER_PTN.matcher(field);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    data.strCallId = match.group(3).replace("(", "").replace(")", "");
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "1ST NORTH",
      "1ST SOUTH",
      "2ND SOUTH",
      "3300 NORTH",
      "3RD SOUTH",
      "4TH SOUTH",
      "ARTHUR BURCH",
      "BELLE AIRE",
      "BIG CHIEF",
      "BROTHER ALPHONSUS",
      "BULL CREEK",
      "BUR OAK",
      "CAREER CENTER",
      "COUNTRY CLUB",
      "DOWN E COURT",
      "EAGLES LANDING",
      "EDGE WATER",
      "EL DORADO",
      "EL PASO",
      "ESSON FARM",
      "EXLINE CLUB",
      "FAMILY DOLLAR",
      "FOX RUN",
      "FRONTAGE EAST",
      "HARVEST VIEW",
      "INDUSTRIAL PARK",
      "JOHN CASEY",
      "LAKE METONGA",
      "LARRY POWER",
      "LITTLE CHIEF",
      "MEADOWS WALK",
      "MID COURT",
      "MILL POND",
      "OAK CREEK",
      "OAK RIDGE",
      "OAK RUN",
      "PARK LANE",
      "PLUM CREEK",
      "PRAIRIE VIEW",
      "RIVER BEND",
      "RIVER NORTH",
      "SAINT FRANCIS",
      "SAINT JOSEPH",
      "SAINT PETERS",
      "SECTION LINE",
      "SLEEPY HOLLOW",
      "SPORTSMAN CLUB",
      "ST GEORGE",
      "ST MICHAELS",
      "ST PAULS",
      "ST PETERS",
      "STEEPLE CHASE",
      "STOCKTON HEIGHTS",
      "STONE RIDGE",
      "TOWN LINE",
      "VAN BUREN",
      "VAN METER",
      "WARNER BRIDGE",
      "WESTERN HILLS",
      "WESTWOOD OAKS",
      "WHITE TAIL",
      "WILL PEOTONE",
      "WILLIAM LATHAM"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "<NEW>",
      "911:UNKNOWN",
      "911:ABANDONED",
      "ABANDONED",
      "ACCIDENT",
      "AGENCY ASSIST",
      "ALARM CALL",
      "ALARM:AUTOMATIC",
      "ALARM:AUTOMATIC/SHAPIRO",
      "ALARM:BOX",
      "ALARM:CO DET",
      "ALARM:FIRE",
      "ALARM:STILL",
      "AMB:ABDOMINAL PAIN",
      "AMB:ALLERGIC REACTION",
      "AMB:BLEEDING",
      "AMB:BREATHING",
      "AMB:CHEST PAIN",
      "AMB:DIABETIC",
      "AMB:FALL",
      "AMB:MUTUAL AID",
      "AMB:SEIZURE",
      "AMB:STROKE",
      "AMB:UNRESP-BREATHING",
      "AMBULANCE",
      "AMBULANCE:ASSIST",
      "ANIMAL CASE",
      "BATTERY",
      "BURGLARY",
      "CONTROL BURN",
      "CRIM DAM PROP",
      "DEATH INVESTIGATION",
      "DECEASED SUBJ",
      "DIS CONDUCT",
      "DISABLED VEH",
      "DISTURBANCE",
      "DOM DIST",
      "DRUG ACTIVITY",
      "DUI",
      "FIGHT",
      "FIRE:AUTO-AIDE",
      "FIRE:BRUSH",
      "FIRE:MUTAL AID",
      "FIRE:STRUCTURE",
      "FIRE:VEHICLE",
      "HAZMAT",
      "HIT/RUN",
      "HOME INVASION",
      "ILLEGAL BURNING",
      "INDECENT EXPOSURE",
      "JUV/RUNAWAY",
      "JUVENILE CALL",
      "KEEP THE PEACE",
      "LIFT ASSIST",
      "MABAS RADIO DRILL",
      "MENTAL CASE",
      "MISSING PERSON",
      "NEW",
      "OTHER DUTIES",
      "OVERDOSE",
      "PERSON UNK",
      "PERSON/ALCOHOL",
      "RADIO DRILL",
      "REMOVAL",
      "RIVERSIDE AMB",
      "ROAD CLOSURE",
      "ROBBERY",
      "SERVICE",
      "SHOTS FIRED",
      "SMOKE/ODOR",
      "SUSPICIOUS ACTIVITY",
      "SUSPICIOUS PERSON",
      "TRAFFIC COMPLAINT",
      "UNKNOWN PROBLEM",
      "VEHICLE CALL",
      "WALK THRU",
      "WEAPONS",
      "WELFARE CHECK",
      "WIRES DOWN"
  );

  private static final String[] CITY_LIST = new String[]{
    "AROMA PARK",
    "BONFIELD",
    "BOURBONNAIS",
    "BRADLEY",
    "BUCKINGHAM",
    "CABERY",
    "CHEBANSE",
    "ESSEX",
    "GRANT PARK",
    "HERSCHER",
    "HOPKINS PARK",
    "IRWIN",
    "KANKAKEE",
    "LIMESTONE",
    "MANTENO",
    "MOMENCE",
    "PEWING",
    "SOLLITT",
    "ST ANNE",
    "REDDICK",
    "SAMMONS POINT",
    "SUN RIVER TERRACE",
    "UNION HILL",
    "WILMINGTON",
    "YEAGER",

    "AROMA TOWNSHIP",
    "BOURBONNAIS TOWNSHIP",
    "ESSEX TOWNSHIP",
    "GANEER TOWNSHIP",
    "KANKAKEE TOWNSHIP",
    "LIMESTONE TOWNSHIP",
    "MANTENO TOWNSHIP",
    "NORTON TOWNSHIP",
    "OTTO TOWNSHIP",
    "PEMBROKE TOWNSHIP",
    "PILOT TOWNSHIP",
    "ROCKVILLE TOWNSHIP",
    "SALINA TOWNSHIP",
    "ST ANNE TOWNSHIP",
    "SUMMER TOWNSHIP",
    "YELLOWHEAD TOWNSHIP",

    "KANKAKEE COUNTY",

    // Ford County
    "FORD COUNTY",
    "ELLIOTT",
    "KEMPTON",
    "LIMESTONE",
    "MELVIN",
    "PAXTON",
    "PIPER CITY",
    "ROBERTS",
    "ROSSVILLE",

    // Grundy County
    "GARDNER",
    "GRUNDY COUNTY",
    "BRACEVILLE",
    "PEOTONE",

    // Iroquois County
    "IROQUOIS COUNTY",
    "CHEBANSE TWP",
    "ASHKUM",
    "CLIFTON",
    "BEAVERVILLE",
    "PAPINEAU",
    "THAWVILLE",

    // Livingston County
    "DWIGHT",

    // Vermillion County
    "VERMILLION COUNTY",
    "EAST LYNN",
    "HOOPESTON",
    "RANKIN",

    // Will County
    "WILL COUNTY",
    "BEECHER",
    "CRETE",
    "BRAIDWOOD",
    "CUSTER PARK",

    // Newton County, IN
    "NEWTON COUNTY",
    "BROOK",
    "GOODLAND",
    "KENTLAND",
    "MOROCCO"
  };

  private static final Properties CITY_STATE_TABLE = buildCodeTable(new String[]{
      "NEWTON COUNTY", "IN",
      "BROOK",         "IN",
      "GOODLAND",      "IN",
      "KENTLAND",      "IN",
      "MOROCCO",       "IN"

  });
}
