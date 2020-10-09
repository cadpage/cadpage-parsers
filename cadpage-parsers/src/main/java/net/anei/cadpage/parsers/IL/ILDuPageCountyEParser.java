package net.anei.cadpage.parsers.IL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILDuPageCountyEParser extends FieldProgramParser {
  
  public ILDuPageCountyEParser() {
    this("DUPAGE COUNTY", "IL");
  }
  
  public ILDuPageCountyEParser(String defCity, String defState) {
    super(defCity, defState, 
          "ADDR ID DATETIME CALL! X INFO! INFO/D+? GPS Disp:UNIT EMPTY? END");
    setupCityValues(CITY_CODES);
    setupGpsLookupTable(ILDuPageCountyParser.GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getAliasCode() {
    return "ILDuPageCountyE";
  }
  
  @Override
  public String getFilter() {
    return "alerts@etsb911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    if (!parseFields(body.split("\\|", -1), data)) return false;
    if (data.strAddress.length() == 0) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("[A-Z]{3}\\d{8}", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new GPSField("[-+]?\\d{2}\\.\\d{2,} [-+]?\\d{2}\\.\\d{2,}|0 0", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CITY_MAP_PTN = Pattern.compile("(.*?)(?:[- ]([A-Z]{2,4}))?(?:[;,](?:(?:APT|LOT|RM|ROOM|UNIT) +)?([-A-Z0-9]+?)(?: \\2)?)?((?: (?:[A-Z]{1,2}\\d{1,3}[A-Z]?|\\d{2,3}[A-Z]|\\d{4}|[A-Z]{2,3}PD)){2})?");
  private static final Pattern DOTS_PTN = Pattern.compile("\\.{2,}");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "@");
      field = stripFieldStart(field, "/");
      String gpsLoc = null;
      if (field.startsWith("LL(")) {
        int pt = field.indexOf(')');
        if (pt >= 0) {
          gpsLoc = field.substring(0, pt+1);
          field = field.substring(pt+1);
        }
      }
      Parser p = new Parser(field);
      String apt = p.getLastOptional(',');
      if (apt.contains(")") || apt.contains(":")) {
        p = new Parser(field);
        apt = "";
      }
      
      field = p.get(':');
      data.strPlace = stripFieldStart(p.get(), "@").replaceAll(": @", " - ");
      if (gpsLoc != null) field = gpsLoc + field;
      field = DOTS_PTN.matcher(field).replaceAll(" ");
      String apt2 = null;
      Matcher match = ADDR_CITY_MAP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        String cityCode = match.group(2);
        apt2 = match.group(3);
        if (apt2 != null && cityCode != null && apt2.equals(cityCode)) apt2 = null;
        data.strMap = getOptGroup(match.group(4));
        if (cityCode != null) {
          String tempCode = cityCode;
          if (tempCode.length() > 2) tempCode = stripFieldEnd(tempCode, "U");
          String city = CITY_CODES.getProperty(tempCode);
          if (city != null) {
            data.strCity = city;
            data.strPlace = stripFieldEnd(data.strPlace, '-' + cityCode);
          } else {
            field = field + ' ' + cityCode;
          }
        }
      }
      if (data.strCity.length() > 0) {
        parseAddress(field, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }
      if (apt2 != null) data.strApt = append(data.strApt, "-", apt2);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY MAP PLACE APT";
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{2}(?:\\d\\d)?) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, match.group(2), data);
      } else {
        data.strTime = time;
      }
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "&");
      field = stripFieldEnd(field, "&");
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AD",   "ADDISON",
      "AE",   "",
      "AF",   "ADDISON",
      "AH",   "ARLINGTON HEIGHTS",
      "AL",   "ALGONQUIN",
      "ANL",  "ARGONNE NATL LABS",
      "AR",   "ARGONNE",
      "AU",   "AURORA",
      "BA",   "BARTLETT",
      "BB",   "BOLINGBROOK",
      "BD",   "BLOOMINGDALE",
      "BE",   "BENSENVILLE",
      "BF",   "BROOKFIELD",
      "BG",   "BUFFALO GROVE",
      "BI",   "BARRINGTON",
      "BK",   "BERKELEY",
      "BL",   "BLOOMINGDALE",
      "BN",   "BERWYN",
      "BO",   "BOLINGBROOK",  // maybe BELLWOOD
      "BP",   "BEDFORD PARK",
      "BR",   "BURR RIDGE",
      "BT",   "BATAVIA",
      "BU",   "BURBANK",
      "BV",   "BATAVIA",
      "BW",   "BROADVIEW",
      "CA",   "CARY",
      "CD",   "COUNTRYSIDE",
      "CG",   "CHICAGO",
      "CH",   "CLARENDON HILLS",
      "CK",   "COOK COUNTY",
      "CL",   "CLAREDON HILLS",
      "CO",   "CICERO",
      "CR",   "CHICAGO RIDGE",
      "CS",   "CAROL STREAM",
      "CV",   "CARPENTERSVILLE",
      "CY",   "CENTRAL STICKNEY",
      "DA",   "DARIEN",
      "DG",   "DOWNERS GROVE",
      "DP",   "DES PLAINES",
      "DW",   "DARIEN-WOODRIDGE",
      "EB",   "ELBURN",
      "ED",   "EAST DUNDEE",
      "EF",   "ELMHURST",
      "EG",   "ELK GROVE VILLAGE",  // Not ELGIN
      "EK",   "ELK GROVE VILLAGE",
      "EL",   "ELMHURST",
      "EN",   "ELGIN",
      "EOL",  "EOLA",
      "EP",   "ELMWOOD PARK",
      "ET",   "ELK GROVE TWP",
      "EV",   "EVERGREEN PARK",
      "FML",  "FERMILAB",
      "FP",   "FRANKLIN PARK",
      "FR",   "FERMILAB",
      "FV",   "FORESTVIEW",
      "FX",   "FOX RIVER",
      "GE",   "GLEN ELLYN",
      "GF",   "GLEN ELLYN",
      "GH",   "GLENDALE HEIGHTS",
      "GL",   "GLENDALE HEIGHTS",
      "GS",   "GLENSIDE",
      "GV",   "GENEVA",
      "HA",   "HANOVER PARK",
      "HF",   "HOFFMAN ESTATES",
      "HI",   "HINSDALE",
      "HK",   "HODGKINS",
      "HM",   "HAMPSHIRE",
      "HN",   "HINSDALE",
      "HP",   "HANOVER PARK",
      "HS",   "HILLSIDE",
      "HT",   "HOMETOWN",
      "IF",   "ITASCA",
      "IHP",  "INDIAN HEAD PARK",
      "IP",   "INDIAN HEAD PARK",
      "IT",   "ITASCA",
      "KC",   "KANE COUNTY",
      "KE",   "KENDALL COUNTY",
      "LB",   "LOCKPORT",
      "LE",   "LEMONT",
      "LG",   "LA GRANGE",
      "LI",   "LISLE",
      "LM",   "LEMONT",
//      "LN",   "LYONS",
      "LO",   "LOMBARD",
      "LP",   "LAGRANGE PARK",
      "LW",   "LISLE- WOODRIDGE",
      "LY",   "LEYDEN",
      "LZ",   "LAKE ZURICH",
      "MC",   "MCCOOK",
      "ME",   "MEDINAH",
      "MED",  "MEDINAH",
      "MK",   "MAPLE PARK",
      "MP",   "MELROSE PARK",
      "MT",   "MOUNT PROSPECT",
      "MW",   "MAYWOOD",
      "NA",   "NORTH AURORA",  // was NAPERVILLE
      "NH",   "NORTHWEST HOMER",
      "NL",   "NORTHLAKE",
      "NP",   "NORTH PALOS",
      "NV",   "NAPERVILLE",
      "NW",   "NORWOOD PARK",
      "OB",   "OAK BROOK",
      "OK",   "OAK BROOK",
      "OL",   "OAK LAWN",
      "OP",   "ORLAND PARK",  // Was OAK BROOK
      "OT",   "OAKBROOK TERRACE",
      "PA",   "PALOS",
      "PF",   "PLAINFIELD",
      "PG",   "PINGREE GROVE",
      "PH",   "PROSPECT HEIGHTS",
      "PR",   "PALATINE RURAL",
      "PS",   "PALOS HEIGHTS",
      "PT",   "PALATINE",
      "PV",   "PLEASANTVIEW",
      "RD",   "RUTLAND DUNDEE",
      "RF",   "ROSELLE",
      "RG",   "RIVER GROVE",
      "RM",   "ROLLING MEADOWS",
      "RO",   "ROSELLE",       // Not ROSEMONT
      "RP",   "ROBERTS PARK",
      "RR",   "RIVERSIDE",
      "RS",   "ROSELEE",
      "RU",   "RUSH OAK PARK",
      "RV",   "ROMEOVILLE",
      "SC",   "ST CHARLES",
      "SE",   "SOUTH ELGIN",
      "SG",   "SUGAR GROVE",
      "SH",   "SCHAUMBURG",
      "SP",   "SCHILLER PARK",
      "ST",   "STONE PARK",
      "STCH", "SAINT CHARLES",
      "SU",   "SUMMIT",
      "SW",   "STREAMWOOD",
      "TS",   "TRI-STATE",
      "VF",   "VILLA PARK",
      "VP",   "VILLA PARK",
      "WA",   "WAYNE",
      "WC",   "WEST CHICAGO",
      "WD",   "WOOD DALE",
      "WE",   "WEST DUNDEE",
      "WF",   "WINFIELD",
      "WG",   "WHEELING",  // was WEST CHICAGO
      "WH",   "WHEATON",
      "WI",   "WINFIELD",
      "WL",   "WILLOWBROOK",
      "WLS",  "WILLOW SPRINGS",
      "WM",   "WESTMONT",
      "WN",   "WINFIELD",
      "WO",   "WOODRIDGE",
      "WP",   "WESTERN SPRINGS",
      "WR",   "WARRENVILLE",
      "WS",   "WESTERN SPRINGS",
      "WT",   "WESTCHESTER",       // Was WHEELING
      "WV",   "WARRENVILLE",
      "WY",   "WILL COUNTY",
      "YC",   "YORK CENTER"

  });
}
