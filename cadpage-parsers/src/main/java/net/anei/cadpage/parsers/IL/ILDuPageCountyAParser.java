package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILDuPageCountyAParser extends FieldProgramParser {
  
  private static final Pattern ZERO_ADDR_PTN1 = Pattern.compile("^0[NSEW]\\d+ ");
  private static final Pattern ZERO_ADDR_PTN2 = Pattern.compile("[NSEW]\\d+");
  
  private static final Pattern MASTER2 = Pattern.compile("\\*!\\* BR \\*!\\* (.*?) @ (.*?)\\[District: *([A-Z0-9]*)\\]");
  private static final Pattern CALL_ID_PTN = Pattern.compile("([A-Z]{3})\\d+");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern DATE_PTN = Pattern.compile("\\d{4}-\\d\\d-\\d\\d");
  
  public ILDuPageCountyAParser() {
    this("DUPAGE COUNTY", "IL");
  }
  
  public ILDuPageCountyAParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! ( DATE:SKIP! TIME:SRC! MAP:MAP! UNIT:UNIT! INFO:INFO! | URL? ) INFO/N+");
  }
  
  @Override
  public String getAliasCode() {
    return "ILDuPageCountyA";
  }
  
  @Override
  public String getFilter() {
    return "DU-COMM@ducomm.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match;
    if (body.startsWith("CALL:")) {
      if (!parseFields(body.split("\n"), 6, data)) return false;
    }
    
    else if (body.startsWith("INC01 1.0 EV-XXX 0       ")) {
      FParser p = new FParser(body);
      p.skip(25);
      if (!p.lookahead(0,15).contains(" ")) {
        setFieldList("ID ADDR APT SRC UNIT CALL PLACE X CITY");
        data.strCallId = p.get(15);
        String addr = p.get(30);
        int pt = addr.indexOf("...");
        if (pt >= 0) addr = addr.substring(0,pt).trim();
        if (ZERO_ADDR_PTN1.matcher(addr).find()) addr = addr.substring(1).trim();
        parseAddress(addr, data);
        data.strSource = p.get(2);
        data.strUnit = p.get(4);
        data.strCall = p.get(20);
        data.strPlace = p.get(45);
        data.strCross = append(p.get(20), " & ", p.get(20));
        p.skip(166);
        String city = p.get(3);
        if (city.endsWith("2") ||
            city.length() > 2 && city.endsWith("U")) {
          city = city.substring(0,city.length()-1);
        }
        data.strCity = convertCodes(city, CITY_CODES);
      }
      
      else {
        setFieldList("UNIT SRC CODE CALL ADDR APT MAP ID CITY DATE TIME");
        data.strUnit = MBLANK_PTN.matcher(p.get(10)).replaceAll(" ");
        data.strSource = p.get(5);
        if (!p.check(" "))  return false;
        data.strCode = p.get(6);
        data.strCall = p.get(20);
        parseAddress(p.get(30), data);
        data.strMap = MBLANK_PTN.matcher(p.get(6)).replaceAll(" ");
        data.strCallId = p.get(15);
        String city = p.get(3);
        if (city.length() == 3 && city.charAt(2) == 'F') city = city.substring(0,2).trim();
        data.strCity = convertCodes(city, CITY_CODES);
        if (p.get(14).length() > 0) return false;
        String date = p.get(10);
        if (!DATE_PTN.matcher(date).matches()) return false;
        data.strDate = date.substring(5,7) + '/' + date.substring(8,10) + '/' + date.substring(0,4);
        if (!p.check(" ")) return false;
        data.strTime = p.get(8);
        return true;
      }
    }
    
    else if ((match = MASTER2.matcher(body)).matches()) {
      setFieldList("CALL ADDR APT UNIT");
      data.strCall = match.group(1).trim();
      parseAddress(match.group(2).trim(), data);
      data.strUnit = match.group(3);
    }
    
    else return false;
    
    match = CALL_ID_PTN.matcher(data.strCallId);
    if (match.matches()) data.strSource = match.group(1);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("ID", "SRC ID");
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("URL")) return new InfoUrlField("http://.*", true);
    return super.getField(name);
  }

  @Override
  public String postAdjustMapAddress(String addr) {
    if (ZERO_ADDR_PTN2.matcher(addr).find()) {
      int pt = addr.indexOf('&');
      if (pt >= 0) addr = addr.substring(0,pt).trim();
    }
    return addr;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AD",     "ADDISON",
      "AE",     "",
      "AF",     "ADDISON",
      "AH",     "ARLINGTON HEIGHTS",
      "AL",     "ALGONQUIN",
      "ANL",    "ARGONNE",
      "AR",     "ARGONNE",
      "AU",     "AURORA",
      "BA",     "BARTLETT",
      // "BA",     "BARRINGTON",   // Conflict
      "BB",     "BOLINGBROOK",
      "BD",     "BLOOMINGDALE",
      "BE",     "BENSENVILLE",
      "BF",     "BROOKFIELD",
      "BG",     "BUFFALO GROVE",
      "BK",     "BERKLEY",
      "BL",     "BLOOMINGDALE",
      // "BL",     "BELLWOOD",     // Conflict
      "BO",     "BOLINGBROOK",
      "BR",     "BURR RIDGE",
      // "BR",     "BARTLETT",
      "BT",     "BATAVIA",
      "BV",     "BATAVIA",
      // "BV",     "BENSENVILLE",
      "BW",     "BROADVIEW",
      "CA",     "CARY",
      "CD",     "COUNTRYSIDE",
      "CG",     "CHICAGO",
      "CH",     "CLARENDON HILLS",
      "CK",     "COOK COUNTY",
      "CL",     "CLAREDON HILLS",
      "CR",     "CAROL STREAM",
      "CS",     "CAROL STREAM",
      "CV",     "CARPENTERSVILLE",
      "D",      "DARIEN",
      "DA",     "DARIEN",
      "DAR",    "DARIEN",
      "DG",     "DOWNERS GROVE",
      "DP",     "DES PLAINES",
      "DW",     "DARIEN-WOODRIDGE",
      "EB",     "ELBURN",
      "ED",     "EAST DUNDEE",
      "EF",     "ELMHURST",
      "EG",     "ELK GROVE VILLAGE",
      "EK",     "ELK GROVE VILLAGE",
      "EL",     "ELMHURST",
      "EN",     "ELGIN",
      // "EL",     "ELGIN",       // Conflict
      "EOL",    "EOLA",
      "ET",     "ELK GROVE TWP",
      "FML",    "FERMILAB",
      "FP",     "FRANKLIN PARK",
      "FR",     "FERMI LAB",
      "FX",     "FOX RIVER",
      "GE",     "GLEN ELLYN",
      "GF",     "GLEN ELLYN",
      "GL",     "GLENDALE HEIGHTS",
      "GS",     "GLENSIDE",
      "GV",     "GENEVA",
      "HA",     "HANOVER PARK",
      "HF",     "HOFFMAN ESTATES",
      "HI",     "HINSDALE",
      "HK",     "HODGKINS",
      "HM",     "HAMPSHIRE",
      "HN",     "HINSDALE",
      "HP",     "HANOVER PARK",
      "HS",     "HILLSIDE",
      "IF",     "ITASCA",
      "IHP",    "INDIAN HEAD PARK",
      "IT",     "ITASCA",
      "KC",     "KANE COUNTY",
      "KE",     "KENDALL COUNTY",
      "LB",     "fLOCKPORT",
      "LE",     "LEMONT",
      "LG",     "LAGRANGE",
      "LI",     "LISLE",
      "LM",     "LEMONT",
      "LO",     "LOMBARD",
      "LP",     "LAGRANGE PARK",
      "LW",     "LISLE- WOODRIDGE",
      "LY",     "LEYDEN",
      "LZ",     "LAKE ZURICH",
      "ME",     "MEDINAH",
      "MED",    "MEDINAH",
      "MK",     "MAPLE PARK",
      "MP",     "MELROSE PARK",
      "MT",     "MOUNT PROSPECT",
      "MW",     "MAYWOOD",
      "NA",     "NAPERVILLE",   
      // "NA",     "NORTH AURORA",
      "NL",     "NORHTLAKE",
      "NV",     "NAPERVILLE",
      "OB",     "OAK BROOK",
      // "OB",     "OAK BROOK TERRACE",
      "OK",     "OAK BROOK",
      "OP",     "OAK BROOK",
      "OT",     "OAK BROOK TERRACE",
      "PF",     "PLAINFIELD",
      "PG",     "PINGREE GROVE",
      "PH",     "PROSPECT HEIGHTS",
      "PR",     "PALATINE RURAL",
      "PT",     "PALATINE",
      "PV",     "PLEASANTVIEW",
      "RD",     "RUTLAND DUNDEE",
      "RF",     "ROSELLE",
      "RM",     "ROLLING MEADOWS",
      "RO",     "ROSELLE",
      // "RO",     "ROSEMONT",    // Conflict
      "RS",     "ROSELEE",
      "RV",     "ROMEOVILLE",
      "SC",     "ST CHARLES",
      "SE",     "SOUTH ELGIN",
      "SH",     "SCHAUMBURG",
      "SP",     "SCHILLER PARK",
      "STCH",   "SAINT CHARLES",
      "SW",     "STREAMWOOD",
      "TS",     "TRI-STATE",
      "VF",     "VILLA PARK",
      "VP",     "VILLA PARK",
      "WA",     "WAYNE",
      "WB",     "WILLOWBROOK",
      "WC",     "WEST CHICAGO",
      // "WC",     "WESTCHESTER",    // Conflict
      "WD",     "WOOD DALE",
      // "WD",     "WEST DUNDEE",    // Conflict
      "WF",     "WINFIELD",
      "WG",     "WEST CHICAGO",
      "WH",     "WHEATON",
      "WI",     "WINFIELD",
      "WL",     "WILLOWBROOK",
      "WLS",    "WILLOW SPRINGS",
      "WM",     "WESTMONT",
      "WN",     "WINFIELD",
      "WO",     "WOODRIDGE",
      "WR",     "WARRENVILLE",
      "WS",     "WESTMONT",
      // "WS",     "WESTERN SPRINGS",
      // "WT",     "WHEATON",
      "WT",     "WHEELING",
      "WV",     "WARRENVILLE",
      "WY",     "WILL COUNTY",
      "YC",     "YORK CENTER"
  });
}
