package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MDAnneArundelCountyAnnapolisParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("^INCIDENT (\\d+)\n\n");
  private static final Pattern DELIM = Pattern.compile("\\n+");
  
  public MDAnneArundelCountyAnnapolisParser() {
    super(CITY_CODES, "ANNE ARUNDEL COUNTY", "MD",
          "CALL1 ADDR/y MAP ZIP! X+? Nature:CALL! Call_back:PHONE? EXTRA+? UNIT UNIT+? INFO+? ( HYDRANTS INFO+? TIMEDATE! | TIMEDATE! )");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "donotreply@annapolis.gov,PrintManager";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Check for duplicated page
    Matcher match = MARKER.matcher(body);
    if (!match.find())  return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end()).trim();
    int pt = body.indexOf(match.group());
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(DELIM.split(body), 7, data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public  Field getField(String name) {
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("EXTRA")) return new MyExtraField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("HYDRANTS")) return new HydrantsField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    return super.getField(name);
  }
  
  private static final Pattern CALL1_PTN = Pattern.compile("CODE +([A-Z0-9]+) +(.*?)( +ALARM 1)?");
  private class MyCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL1_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(2).trim();
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get("   "), data);
      data.strPlace = p.getLast("   ");
      data.strApt = p.get();
      if (data.strApt.length() == 0 && NUMERIC.matcher(data.strPlace).matches()) {
        data.strApt = data.strPlace;
        data.strPlace = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " APT PLACE";
    }
  }
  
  private static final Pattern MAP_PTN = Pattern.compile("Grid +([^ ]+) +Map +([^ ]*) +Area ([^ ]+) +Preplan +([^ ]*) +Channel +([^ ]*) +MOA.*");
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strMap = match.group(1) + '-' + match.group(2) + '-' + match.group(3);;
      String preplan = match.group(4);
      if (preplan.length() > 0) {
        data.strSupp = "Preplan:" + preplan;
      }
      data.strChannel = match.group(5).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "MAP CH";
    }
  }
  
  private static final Pattern ZIP_PTN = Pattern.compile("Census Tract .* Zip(?: +(\\d{5})|)");
  private class MyZipField extends ZipField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ZIP_PTN.matcher(field);
      if (!match.matches()) abort();
      String zip = match.group(1);
      if (zip != null && data.strCity.length() == 0) data.strCity = zip;
    }
  }
  
  private static final Pattern X_PTN = Pattern.compile(".*? cross street - +(.*?)(?:-([A-Z]{2}))?");
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = X_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCross = append(data.strCross, " & ", match.group(1).trim());
      String city = match.group(2);
      if (city != null && 
          (data.strCity.length() == 0 || Character.isDigit(data.strCity.charAt(0)))) {
        data.strCity = convertCodes(city, CITY_CODES);
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern CALL_PTN = Pattern.compile("([A-Z0-9]+)-(.*)", Pattern.CASE_INSENSITIVE);
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = append(data.strCall, " - ", match.group(2).trim());
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern EXTRA_MARKER = Pattern.compile(" +\\([-0-9]+\\)$");
  private static final Pattern EXTRA_PHONE_PTN = Pattern.compile("\\d{10}");
  private static final Pattern EXTRA_SKIP_PTN = Pattern.compile("^VERIFY|^TAC CHANNEL|^Added unit |^Remove unit |^VOIP CALL QUERY|^WIRELESS CALLER VERIFY LOCATION|^New Fire Incident location:|^Fire service incident | changed from[ :]| Changed Via ProQA | had changed quarters for |'on air'");
  private class MyExtraField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("*")) field = field.substring(1).trim();
      Matcher match = EXTRA_MARKER.matcher(field);
      if (match.find()) field = field.substring(0, match.start());
      if (field.startsWith("Community of ")) {
        String city = field.substring(13).trim();
        if (city.endsWith(" CITY")) city = city.substring(0,city.length()-5).trim();
        if (city.equals("ANNAPOLIS")) {
          if (data.strCity.length() == 0) data.strCity = city;
        } 
        else if (data.strPlace.length() == 0) {
          data.strPlace = city;
        }
        return;
      }
      
      match = EXTRA_PHONE_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = field;
        return;
      }
      
      if (EXTRA_SKIP_PTN.matcher(field).find()) return;
      if (field.startsWith("ProQaA:")) field = field.substring(7).trim();
      if (field.equals("-")) return;
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "PHONE PLACE INFO";
    }
  }
  
  private class MyUnitField extends UnitField {
    public MyUnitField() {
      super("(?:(?:^| +)(?:[A-Z]{2}\\.[A-Z]{2}|\\d?[A-Z]{1,4}\\d{1,3}|[A-Z]{3,4}))+", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  private class HydrantsField extends InfoField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("HYDRANTS:")) return false;
      if (field.equals("HYDRANTS:  () &  ()")) return true;
      if (field.equals("HYDRANTS:  (0) &  (0)")) return true;
      data.strSupp = append(data.strSupp, "\n", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern TIME_DATE_PTN = Pattern.compile("TIME (\\d\\d:\\d\\d:\\d\\d) +DATE (\\d\\d/\\d\\d/\\d\\d)");
  private class MyTimeDateField extends TimeDateField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = TIME_DATE_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strTime = match.group(1);
      data.strDate = match.group(2);
      return true;
    }
    
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = DIR_PTN.matcher(address).replaceAll("");
    address = RT_BLANK_PTN.matcher(address).replaceAll("$1$2");
    address = GENERALS_HWY_PTN.matcher(address).replaceAll("RT178");
    address = BALTO_ANNAP_BLVD_PTN.matcher(address).replaceAll("RT648");
    address = NEW_CUT_RD_PTN.matcher(address).replaceAll("CRAIN HWY");
    address = DORSEY_RD_PTN.matcher(address).replaceAll("RT176");
    address = QUARTERFIELD_RD_PTN.matcher(address).replaceAll("RT174");
    return address;
  }
  private static final Pattern DIR_PTN = Pattern.compile(" +[NSEW]\\b");
  private static final Pattern RT_BLANK_PTN = Pattern.compile("(I|RT) +(\\d+)");
  private static final Pattern GENERALS_HWY_PTN = Pattern.compile("\\bGENERALS HWY\\b");
  private static final Pattern BALTO_ANNAP_BLVD_PTN = Pattern.compile("\\bBALTO ANNAP BLVD\\b");
  private static final Pattern NEW_CUT_RD_PTN = Pattern.compile("\\bNEW CUT RD\\b");
  private static final Pattern DORSEY_RD_PTN = Pattern.compile("\\bDORSEY RD\\b");
  private static final Pattern QUARTERFIELD_RD_PTN = Pattern.compile("\\bQUARTERFIELD RD\\b");

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "I97 & CROWNSVILLE RD",        "38.99683316,-76.58685279",
      "I97 & DEFENSE HWY",           "38.98939223,-76.57863063",
      "I97 & FARM RD",               "39.01858781,-76.61416776",
      "I97 & HAWKINS RD",            "39.00769803,-76.60822717",
      "I97 & RT50",                  "38.98445298,-76.56910571",
      "I97 & CROWNSVILLE EXIT",      "39.04721747,-76.62119272",
      "I97 & RT178",                 "39.04721747,-76.62119272",
      "I97 & RT32",                  "39.06268206,-76.64011324",
      "I97 & WATERBURY RD",          "39.04768415,-76.62243167",
      "I97 & RT648",                 "39.17830710,-76.63466287",
      "I97 & CRAIN HWY",             "39.12832682,-76.64174212",
      "I97 & CROMWELL PARK DR",      "39.17261419,-76.63892790",
      "I97 & RT176",                 "39.16595633,-76.64226694",
      "I97 & FURNACE BRANCH RD",     "39.19735299,-76.63233282",
      "I97 & I695",                  "39.20081562,-76.63159823",
      "I97 & STEWART AVE",           "39.16080315,-76.64433820",
      "I97 & I895",                  "39.20649600,-76.62699700",
      "I97 & WELLHAM AVE",           "39.18516057,-76.63232174",
      "I97 & RT174",                 "39.14462105,-76.64487056",
      "I97 & RT100",                 "39.15464226,-76.64579945",
      "I97 & BENFIELD BLVD",         "39.09459880,-76.62947010",
      "I97 & BRIGHTVIEW DR",         "39.11097321,-76.63255299",
      "I97 & MILLERSVILLE RD",       "39.05671991,-76.63138975",
      "I97 & RT3",                   "39.06718618,-76.64020238",
      "I97 & VETERANS HWY",          "39.12114900,-76.63808700"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AN",   "ANNAPOLIS",
      "AR",   "ARNOLD",
      "BB",   "BEVERLY BEACH",
      "BP",   "BROOKLYN PARK",
      "CH",   "CHURCHTON",
      "CM",   "CARROLLTON MANOR",
      "CP",   "COLONIAL PARK",
      "CR",   "CROFTON",
      "CV",   "CROWNSVILLE",
      "DK",   "DUNKIRK",
      "DL",   "DEALE",
      "DV",   "DAVIDSONVILLE",
      "EP",   "EASTPORT",
      "EW",   "EDGEWATER",
      "FM",   "FORT MEADE",
      "FR",   "FRIENDSHIP",
      "GB",   "GLEN BURNIE",
      "GI",   "GIBSON ISLAND",
      "GM",   "GAMBRILLS",
      "GV",   "GALESVILLE",
      "HA",   "HANOVER",
      "HB",   "HIGHLAND BEACH",
      "HS",   "HILLSMERE SHORES",
      "HW",   "HARWOOD",
      "JS",   "JESSUP",
      "LH",   "LINTHICUM HEIGHTS",
      "LO",   "LOTHIAN",
      "LS",   "LAKE SHORE",
      "MC",   "MARYLAND CITY",
      "MPB",  "MOUNT PLEASANT BEACH",
      "MV",   "MILLERSVILLE",
      "OB",   "ORCHARD BEACH",
      "OD",   "ODENTON",
      "PA",   "PASADENA",
      "RB",   "RIVIERA BEACH",
      "RV",   "RIVA",
      "SF",   "SHERWOOD FOREST",
      "SP",   "SEVERNA PARK",
      "SR",   "SOUTH RIVER", 
      "SS",   "SHADY SIDE",
      "SV",   "SEVERN",
      "TL",   "TRACEYS LANDING",
      "UMB",  "UPPER MAGOTHY BEACH",
      "WR",   "WEST RIVER"
  });
}
