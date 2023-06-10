package net.anei.cadpage.parsers.DE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DENewCastleCountyEParser extends FieldProgramParser {

  public DENewCastleCountyEParser() {
    super("NEW CASTLE COUNTY", "DE",
          "Tac_#:CH? Date/Time:DATETIME! Call_Address:ADDRCITY/S6Xa! Common_Name:PLACE! Cross_Streets:X! Local_Info:PLACE! Development:CITY? Type:CODE_CALL! Narrative:INFO! INFO/N+ EMS_Dist:MAP! Fire_Quad:MAP! Inc_Numbers:ID! Units_Assigned:UNIT! Alerts:INFO/N! INFO/N+ Status_Times:TIMES+ MAP:SKIP");
  }

  @Override
  public String getFilter() {
    return "NW911@state.de.us";
  }

  private static final Pattern MARKER = Pattern.compile("NCC911 Rip & Run *(?:: *\\d+ *)?\\n");

  private Set<String> unitSet = new HashSet<String>();
  private String timeInfo;

  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();

    unitSet.clear();
    timeInfo = "";
    if (!parseFields(body.split("\n"), data)) return false;
    fixCity(data);
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(timeInfo, "\n", data.strSupp);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("TIMES")) return new TimesField();
    return super.getField(name);
  }

  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d( [AP]M)?)", Pattern.CASE_INSENSITIVE);
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (!parseDateTime(field, data)) abort();
    }
  }

  private static final Pattern ADDR_ST_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAddressCityField extends AddressCityField {

    @Override
    public void parse(String field, Data data) {
      if (field.equals("Qual:")) return;
      Parser p = new Parser(field);
      String apt = p.getLastOptional(" Qual:");
      String city = p.getLastOptional(',');
      if (ADDR_ST_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      if (city.toUpperCase().startsWith("INTERSTATE")) city = "";
      data.strCity = city;
      field = p.get().replace('@', '&');

      // Check for dash delimited city
      field = checkDashCity(field, data);
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      if (!data.strApt.contains(apt)) data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  static String checkDashCity(String field, Data data) {
    String city;
    if (data.strCity.length() == 0) {
      int pt = field.lastIndexOf('-');
      city = field.substring(pt+1).trim();
      if (CITY_SET.contains(city.toUpperCase())) {
        data.strCity = city;
        field = field.substring(0,pt).trim();
      }
    }
    return field;
  }

  private static final Pattern PLACE_APT_PTN = Pattern.compile("(.*?)[-, ]*(?:\\b(?:RM|ROOM|APT|LOT|COTTAGE|SUITE)\\b|(?<!REF) #)[ #]*(.+)", Pattern.CASE_INSENSITIVE);
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {

      // Check for duplicated address line
      int pt = field.lastIndexOf('-');
      if (pt >= 0) {
        if (data.strAddress.startsWith(field.substring(pt+1).trim())) {
          field = field.substring(0,pt).trim();
        }
      }

      // Check for apt number
      Matcher match =  PLACE_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        String apt = match.group(2);
        if (!data.strApt.contains(apt)) {
          data.strApt = append(data.strApt, "-", apt);
        }
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals ("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }

  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.equals("Priority:")) return;
      Parser p = new Parser(field);
      String call = p.get(" Priority:");
      String pri = p.get();
      call = suppressDup(call);
      data.strPriority = suppressDup(pri);

      int pt = call.indexOf(' ');
      if (pt < 0) abort();
      data.strCode = call.substring(0,pt).trim();
      data.strCall = call.substring(pt+1).trim();
    }

    private String suppressDup(String field) {
      int pt = field.indexOf(',');
      if (pt < 0) return field;
      String part1 = field.substring(0,pt).trim();
      String part2 = field.substring(pt+1).trim();
      if (part1.equals(part2)) return part1;
      return field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL PRI";
    }
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (data.strMap.length() == 0) {
        data.strMap = field;
        return;
      }
      if (field.equals(data.strMap)) return;
      data.strMap = "E:" + data.strMap + " F:" + field;
    }
  }

  private static final Pattern UNIT_DELIM_PTN = Pattern.compile("[, ]+");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      for (String unit : UNIT_DELIM_PTN.split(field)) {
        addUnit(unit, data);
      }
    }
  }

  private class TimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.startsWith("Unit:")) {
        addUnit(field.substring(5).trim(), data);
      } else {
        if (data.strTime.length() == 0) {
          if (field.startsWith("Dispatched at:")) {
            String time = field.substring(14).trim();
            int pt = time.indexOf('\t');
            if (pt < 0) pt = time.indexOf("  ");
            if (pt >= 0) time = time.substring(0,pt).trim();
            parseDateTime(time, data);
          }
        }
        if (field.indexOf("Cleared at:") >= 0) data.msgType = MsgType.RUN_REPORT;
        field = "  " + field.replace("\t", "\n  ");
      }
      timeInfo = append(timeInfo, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "UNIT DATE TIME INFO";
    }
  }

  private void addUnit(String unit, Data data) {
    if (unitSet.add(unit)) data.strUnit = append(data.strUnit, " ", unit);
  }

  private boolean parseDateTime(String field, Data data) {
    Matcher match = DATE_TIME_PTN.matcher(field);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    if (match.group(3) != null) {
      setDateTime(TIME_FMT, match.group(2), data);
    } else {
      data.strTime = match.group(2);
    }
    return true;
  }

  private static final Pattern DEMOTE_CITY_PTN =
      Pattern.compile(".*\\b(?:TRAILER PARK|PLAZA|(SHOP(?:PING)? (?:CTR|CENTER))|APARTMENTS|APTS|CONDOS|TOWNHOUSES|TWNHSES|MALL|PROFESSIONAL|HOUSE|CENTER|TOWER|CLUB|AIRPORT)\\b.*", Pattern.CASE_INSENSITIVE);
  static void fixCity(Data data) {

    // Sometimes the city field obviously should not be a city
    if (DEMOTE_CITY_PTN.matcher(data.strCity).matches()) {
      String oldPlace = data.strPlace.toUpperCase();
      String newPlace = data.strCity.toUpperCase();
      if (!oldPlace.contains(newPlace)) {
        if (newPlace.contains(oldPlace)) {
          data.strPlace = data.strCity;
        } else {
          data.strPlace = append(data.strPlace, " - ", data.strCity);
        }
      }
      data.strCity = getMapCity(data.strCity);
    }

    String upCity = data.strCity.toUpperCase();
    String tmp = MISSPELLED_CITIES.getProperty(upCity);
    if (tmp != null) {
      data.strCity = tmp;
      upCity = tmp.toUpperCase();
    }

    if (upCity.endsWith(" CO")) {
      if (data.strCity.endsWith("O")) {
        data.strCity += "UNTY";
      } else {
        data.strCity += "unty";
      }
      upCity = data.strCity.toUpperCase();
    }

    // Some cities are in other states
    if (data.strState.length() == 0) {
      String st = CITY_ST_TABLE.getProperty(upCity);
      if (st != null) data.strState = st;
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return adjustMapAddressStatic(addr);
  }

  static String adjustMapAddressStatic(String addr) {
    addr = PZ_PTN.matcher(addr).replaceAll("PLAZA");
    return addr;
  }
  private static final Pattern PZ_PTN = Pattern.compile("\\bPZ\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapCity(String city) {
    return adjustMapCityStatic(city);
  }

  static String adjustMapCityStatic(String city) {
    if (city.length() == 0) return "";
    city = getMapCity(city);
    if (city.length()== 0) city = "NEW CASTLE COUNTY";
    return city;
  }

  private static String getMapCity(String city) {
    String upCity = city.toUpperCase();
    if (CITY_SET.contains(upCity)) return city;
    city = MAP_CITY_TABLE.getProperty(upCity);
    if (city != null) return city;
    return "";
  }

  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(
      "ALAPOCAS",
      "ARDEN",
      "ARDENCROFT",
      "ARDENTOWN",
      "BEAR",
      "BELLEFONTE",
      "BROOKSIDE",
      "CHRISTIANA",
      "CLAYMONT",
      "CLAYTON",
      "COLLINS PARK",
      "DELAWARE CITY",
      "EDGEMOOR",
      "ELSMERE",
      "FAIRFAX",
      "GASGOW",
      "GRANOGUE",
      "GREENVILLE",
      "GWINHURST",
      "HOCKESSIN",
      "HOLLY OAK",
      "INDIAN FIELD",
      "LAUREL CROSSING",
      "MIDDLETOWN",
      "MILL CREEK",
      "MINQUADALE",
      "MONTCHANIN",
      "MONTCHANIN VILLAGE",
      "NEW CASTLE",
      "NEWARK",
      "NEWPORT",
      "NORTH STAR",
      "ODESSA",
      "OGLETOWN",
      "PIKE CREEK",
      "PORT PENN",
      "ROCKLAND",
      "SMYRNA",
      "SPRING VALLEY",
      "ST GEORGES",
      "STANTON",
      "TALLEYVILLE",
      "TOWNSEND",
      "WESTOVER HILLS",
      "WILMINGTON",
      "WILMINGTON MANOR",
      "WINTERTHUR",
      "WOODDALE",
      "YORKLYN",

      "CHESTER CO",
      "CHESTER COUNTY",
      "DELAWARE CO",
      "DELAWARE COUNTY",
        "ASTON",
        "ASTON TWP",
        "BETHEL TWNSHP",
        "BETHEL TWP",
        "GARNET VALLEY",
        "UPPER CHI",
        "UPPER CHICHESTER TWP",
        "UPPER CHI TWNSHP",
      "GLOUCESTER CO",
      "GLOUCESTER COUNTY",
      "SALEM CO",
      "SALEM COUNTY",
      "KENT CO",
      "KENT COUNTY",
      "CECIL CO",
      "CECIL COUNTY"
  ));

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "BETHEL TWNSHP",          "Bethel Twp",
      "UPPER CHI",              "Upper Chichester Twp",
      "UPPER CHI TWNSHP",       "Upper Chichester Twp"
  });

  private static final Properties CITY_ST_TABLE = buildCodeTable(new String[]{
      "CHESTER COUNTY",         "PA",
      "DELAWARE COUNTY",        "PA",
      "BETHEL TWP",             "PA",
      "GARNET VALLEY",          "PA",
      "UPPER CHICHESTER TWP",   "PA",
      "GLOUCESTER COUNTY",      "NJ",
      "SALEM COUNTY",           "NJ",
      "CECIL COUNTY",           "MD"
  });

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{

      "GOVERNORS SQUARE",             "Bear",

      "ASHBOURNE HILLS",              "Claymont",
      "BNAI BRITH HOUSE",             "Claymont",
      "BRANDYWINE CORPORATE CENTER",  "Claymont",
      "KNOLLWOOD",                    "Claymont",
      "NAAMANS APARTMENTS",           "Claymont",
      "NORTHTOWNE PLAZA",             "Claymont",
      "RADNOR WOODS",                 "Claymont",
      "RIVERSIDE",                    "Claymont",
      "ROLLING PARK",                 "Claymont",
      "STONEYBROOK APARTMENTS",       "Claymont",
      "TRI STATE MALL UPPER LEVEL",   "Claymont",
      "WATER VIEW COURT APARTMENTS",  "Claymont",
      "WHITNEY PRESIDENTIAL TOWER 2000","Claymont",
      "WOODSTREAM GARDEN APARTMENTS", "Claymont",

      "ELSMERE PARK APARTMENTS",      "Elsmere",

      "STONEWOLD",                    "Greenville",

      "ADARE VILLAGE",                "Hockessin",
      "AUTUMNWOOD",                   "Hockessin",
      "BON AYRE",                     "Hockessin",
      "COKESBURY VILLAGE",            "Hockessin",
      "CRICKET HILL",                 "Hockessin",
      "GATEWAY FARMS",                "Hockessin",
      "GATEWAY TOWNHOUSES",           "Hockessin",
      "HOCKESSIN CENTER",             "Hockessin",
      "HOCKESSIN HUNT",               "Hockessin",
      "HOCKESSIN VALLEY FALLS",       "Hockessin",
      "HORSESHOE HILLS",              "Hockessin",
      "LANTANA SQUARE SHOPPING CTR.", "Hockessin",
      "MEWS AT HOCKESSIN",            "Hockessin",
      "NINE GATES VALLEY",            "Hockessin",
      "QUAKER HILL",                  "Hockessin",
      "ROLLING RIDGE",                "Hockessin",
      "SNUG HILL",                    "Hockessin",
      "STENNING WOODS",               "Hockessin",
      "STONE MILL",                   "Hockessin",
      "STUYVESANT HILLS",             "Hockessin",
      "THORNBERRY",                   "Hockessin",
      "WALNUT HILL",                  "Hockessin",
      "WELLINGTON HILLS",             "Hockessin",
      "WELLINGTON PLAZA",             "Hockessin",
      "WESTWOODS",                    "Hockessin",

      "064/284",                      "Middletown",
      "EVERGREEN FARMS",              "Middletown",
      "LONG MEADOW",                  "Middletown",

      "ARBOR PLACE",                  "New Castle",
      "EDEN PARK GARDENS",            "New Castle",
      "GARFIELD PARK",                "New Castle",
      "HARES CORNER",                 "New Castle",
      "HOLLOWAY TERRACE",             "New Castle",
      "IVYRIDGE",                     "New Castle",
      "JEFFERSON FARMS",              "New Castle",
      "LLANGOLLEN ESTATES",           "New Castle",
      "MALLARD POINTE",               "New Castle",
      "MINQUADALE TRAILER PARK",      "New Castle",
      "NEW CASTLE CORPORATE COMMONS", "New Castle",
      "NEW CASTLE COUNTY AIRPORT",    "New Castle",
      "OAKMONT",                      "New Castle",
      "OVERVIEW GARDENS",             "New Castle",
      "PENN ACRES",                   "New Castle",
      "ROSE HILL",                    "New Castle",
      "SIMONDS GARDENS",              "New Castle",
      "SWANWYCK",                     "New Castle",
      "WILTON",                       "New Castle",

      "ALTERSGATE",                   "Newark",
      "BEECH HILL",                   "Newark",
      "BROOKSIDE PARK",               "Newark",
      "CARRINGTON WAY APARTMENTS",    "Newark",
      "CHERRY HILL",                  "Newark",
      "CHESTNUT VALLEY",              "Newark",
      "DELAPLANE MANOR",              "Newark",
      "FAIRFIELD",                    "Newark",
      "FOXWOOD APARTMENTS",           "Newark",
      "GEORGE READ VILLAGE",          "Newark",
      "HARBOR CLUB APARTMENTS",       "Newark",
      "HILLSTREAM",                   "Newark",
      "JARRELL FARMS",                "Newark",
      "LAMBETH RIDING",               "Newark",
      "MEADOWDALE",                   "Newark",
      "PENCADER CORPORATE CENTER",    "Newark",
      "PEOPLES PLAZA",                "Newark",
      "POLLY DRUMMOND SHOPPING CENTER","Newark",
      "RIDGEWOOD GLEN",               "Newark",
      "TODD ESTATES",                 "Newark",
      "UNIVERSITY OF DELAWARE",       "Newark",
      "VICTORIA MEWS APTS",           "Newark",
      "WOODLAND VILLAGE",             "Newark",

      "ALBAN PARK",                   "Wilmington",
      "ASHLEY",                       "Wilmington",
      "AUGUSTINE PROFESSIONAL CENTER","Wilmington",
      "AUGUSTINE RIDGE",              "Wilmington",
      "BALLYMEADE",                   "Wilmington",
      "BARLEY MILL PLAZA",            "Wilmington",
      "BELVEDERE",                    "Wilmington",
      "BOXWOOD INDUSTRIAL PARK",      "Wilmington",
      "BOXWOOD",                      "Wilmington",
      "BRANDON",                      "Wilmington",
      "BRANDYWOOD",                   "Wilmington",
      "BROOKLAND TERRACE",            "Wilmington",
      "CANBY PARK SHOPPING CENTER",   "Wilmington",
      "CANNERY SHOPPING CENTER",      "Wilmington",
      "CARILLON CROSSING",            "Wilmington",
      "CEDAR HEIGHTS",                "Wilmington",
      "CHATHAM",                      "Wilmington",
      "CLELAND HEIGHTS",              "Wilmington",
      "COLONIAL HEIGHTS",             "Wilmington",
      "COLONIAL WOODS",               "Wilmington",
      "CONCORD GALLERY",              "Wilmington",
      "CONCORD PLAZA",                "Wilmington",
      "COUNTRY GATES",                "Wilmington",
      "CRANSTON HEIGHTS",             "Wilmington",
      "DEER VALLEY",                  "Wilmington",
      "DEPOT SHOPPING CENTER",        "Wilmington",
      "DEVON",                        "Wilmington",
      "DREXEL",                       "Wilmington",
      "DUNLEITH",                     "Wilmington",
      "DUNLINDEN ACRES",              "Wilmington",
      "ELSMERE MANOR",                "Wilmington",
      "FAIRWAY FALLS",                "Wilmington",
      "FOREST BROOK GLEN",            "Wilmington",
      "FOULK WOODS",                  "Wilmington",
      "FOULKSTONE PLAZA",             "Wilmington",
      "FURNACE CREEK HOLLOW",         "Wilmington",
      "GORDON HEIGHTS",               "Wilmington",
      "GORDY ESTATES",                "Wilmington",
      "GRAYLYN CREST",                "Wilmington",
      "GRAYSTONE PLAZA SHOPPING CTR.", "Wilmington",
      "GRENDON FARMS",                "Wilmington",
      "HIDDEN VALLEY APARTMENTS",     "Wilmington",
      "HIGHLAND WEST",                "Wilmington",
      "HOLIDAY HILLS",                "Wilmington",
      "KIAMENSI GARDENS",             "Wilmington",
      "LANCASHIRE",                   "Wilmington",
      "LIMESTONE HILLS",              "Wilmington",
      "LONGVIEW COURT APARTMENTS",    "Wilmington",
      "LONGVIEW FARMS",               "Wilmington",
      "MARYLAND PARK APARTMENTS",     "Wilmington",
      "MERIDEN",                      "Wilmington",
      "MIDWAY PLAZA SHOPPING CENTER", "Wilmington",
      "MILL CREEK VILLAGE APARTMENTS","Wilmington",
      "MURRAY MANOR",                 "Wilmington",
      "NORTHPOINTE AT LIMESTONE HILLS","Wilmington",
      "NORTHWOOD",                    "Wilmington",
      "OAK LANE MANOR",               "Wilmington",
      "OWLS NEST",                    "Wilmington",
      "PALADIN CLUB",                 "Wilmington",
      "PARK PLACE TRAILER PARK",      "Wilmington",
      "PENNROSE",                     "Wilmington",
      "PLEASANT HILLS",               "Wilmington",
      "PRICES CORNER SHOPPING CENTER","Wilmington",
      "RAMBLEWOOD",                   "Wilmington",
      "RICHARDSON PARK",              "Wilmington",
      "ROCKLAND CENTER",              "Wilmington",
      "SILVERBROOK APARTMENTS",       "Wilmington",
      "SILVIEW",                      "Wilmington",
      "SOUTHBRIDGE",                  "Wilmington",
      "ST. GEORGES COURT APARTMENTS", "Wilmington",
      "STRATFORD APARTMENTS",         "Wilmington",
      "SUMMIT CHASE",                 "Wilmington",
      "TALLEYVILLE SHOP CTR",         "Wilmington",
      "THE MEADOWS",                  "Wilmington",
      "TUXEDO PARK",                  "Wilmington",
      "TYBROOK",                      "Wilmington",
      "VALLEY RUN TWNHSES",           "Wilmington",
      "VILLAGE OF HERSHEY RUN CONDOS","Wilmington",
      "WINDYBUSH",                    "Wilmington",
      "WINTERSET FARMS TRAILER PARK", "Wilmington",
      "WOODCREST",                    "Wilmington",
      "WOODLAND APARTMENTS",          "Wilmington",
      "WYCLIFFE",                     "Wilmington",
      "WYNNWOOD",                     "Wilmington",

      "LEEDOM ESTATES",               "Wilmington Manor",
      "MANOR PARK",                   "Wilmington Manor"

  });
}
