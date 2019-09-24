package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class PAColumbiaCountyBParser extends FieldProgramParser {
  
  public PAColumbiaCountyBParser() {
    super(CITY_CODES, "COLUMBIA COUNTY", "PA", 
          "( SELECT/1 DATETIME1 Inc_Addr:ADDRCITY/S! Apt:APT! Cross_Streets:X1? Caller:NAME? Phone#:PHONE! Callback#:PHONE/L! GPS! Inc_Code:CODE_CALL1! SubType:CALL/SDS!  Unit_RunTimes:EMPTY! TIMES+ CFS#:ID! Comments:EMPTY! INFO/N+ " + 
          "| ( DR_ID DR_ID+? ADDRCITY/S GPS ( CODE_CALL3 | CODE_CALL2! CALL/SDS ) " + 
            "| ADDRCITY/S ( CODE_CALL2 CALL/SDS DR_ID+? GPS! " +
                         "| CODE_CALL3 DR_ID+? GPS! " +
                         "| GPS ( CODE_CALL2! CALL/SDS! | CODE_CALL3! " + 
                         ") " + 
            ") " + 
          ") Disp_Time:DATETIME! Units:UNIT! UNIT/S+ Comments:INFO/N+ )");
    setupProtectedNames("TWO AND ONE HALF");
    setupCities(CITY_LIST);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@columbiapa.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Page for CFS ([EF]?\\d{6}-\\d{1,3})");
  private static final Pattern RESPONDING_UNITS_PTN = Pattern.compile("Responding Unit\\(s\\):(?: +Units:)?");
  private static final Pattern BRK_PTN = Pattern.compile("\n|(?<!\n)(?=Disp Time:)");
  private static final Pattern NAME_CITY_PTN = Pattern.compile("(.*) 911");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.equals("Archived CFS Report")) {
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("1");
    } else {
      Matcher match = SUBJECT_PTN.matcher(subject);
      if (!match.matches()) return false;
      data.strCallId = match.group(1);
      setSelectValue("2");
      body = body.replace("Disp Time:", " Disp Time:");
      body = RESPONDING_UNITS_PTN.matcher(body).replaceAll("Units:");
    }
    
    if (!parseFields(BRK_PTN.split(body), data)) return false;
    
    if (data.strCity.length() == 0) {
      Matcher match = NAME_CITY_PTN.matcher(data.strName);
      if (match.matches()) {
        String city = match.group(1).trim().toUpperCase();
        if (isCity(city)) data.strCity = convertCodes(city, CITY_CODES);
      }
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new SkipField("[A-Z][a-z]{2} [A-Z][a-z]{2} \\d\\d \\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CODE_CALL1")) return new MyCodeCall1Field();
    if (name.equals("CODE_CALL2")) return new MyCodeCall2Field();
    if (name.equals("CODE_CALL3")) return  new MyCodeCall3Field();
    if (name.equals("X1")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("DR_ID")) return new SkipField("DR#:.*", true);
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" * ", " / ");
      super.parse(field, data);
    }
  }

  private static final Pattern LEAD_UNIT_PTN = Pattern.compile("U:(\\S+) +(.*)");
  private static final Pattern[] ADDR_CITY_PTNS = new Pattern[]{
      Pattern.compile("(.*)(?:[\\(,]| \\*\\* )(.*?)(?:\\)|\\*\\*)?"),
      Pattern.compile("(.*):([A-Z ]+):.*")
  };
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      
      String apt = "";
      Matcher match = LEAD_UNIT_PTN.matcher(field);
      if (match.lookingAt()) {
        apt = match.group(1);
        field = match.group(2);
      }
      
      super.parse(field, data);
      data.strAddress = stripFieldEnd(data.strAddress, ";");
      data.strApt = append(apt, "-", data.strApt);

      data.strCity = convertCodes(data.strCity.toUpperCase(), CITY_CODES);
      data.strCity = stripFieldEnd(data.strCity, " B");
      if (data.strCity.endsWith(" T")) data.strCity += "WP";
      data.strCity = data.strCity.replace("  ", " ");
      
      for (Pattern ptn : ADDR_CITY_PTNS) {
        match = ptn.matcher(data.strAddress);
        if (match.matches()) {
          String city = match.group(2).trim().toUpperCase();
          city = stripFieldEnd(city, "BORO");
          if (isCity(city)) {
            data.strAddress = match.group(1).trim();
            data.strCity =  convertCodes(city, CITY_CODES);
          }
          break;
        }
      }
      
      if (data.strCity.endsWith(" CO") || data.strCity.endsWith(" COUNTY")) {
        Result result = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, data.strAddress);
        if (result.getCity().length() > 0) {
          data.strAddress = "";
          result.getData(data);
        }
      }
    }
    
    @Override
    public void setNoCity(boolean noCity) {}
    
  }
  
  private static final Pattern CALL_CODE1_PTN = Pattern.compile("([_+A-Z0-9]+) : ([^:]*)");
  private class MyCodeCall1Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL_CODE1_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern CALL_CODE2_PTN = Pattern.compile("([_+A-Z0-9]+) : ([^:]*?)(?: :)?");
  private class MyCodeCall2Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals(":  :")) return true;
      Matcher match = CALL_CODE2_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern CALL_CODE3_PTN = Pattern.compile("([_+A-Z0-9]+) : (.*?) : (.*)");
  private class MyCodeCall3Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL_CODE3_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCode = match.group(1);
      data.strCall = append(match.group(2).trim(), " - ", match.group(3).trim());
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("http://maps.google.com/maps\\?q=(.*?)%20(.*?)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return;
      setGPSLoc(match.group(1)+','+match.group(2), data);
    }
  }
  
  private static final Pattern TIMES_UNIT_PTN = Pattern.compile("([-A-Z0-9]+) +DISP +.*");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_UNIT_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = append(match.group(1), " ", data.strUnit);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT INFO";
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[\\d\\d:\\d\\d:\\d\\d [A-Za-z ]+\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_JUNK_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = stripFieldStart(addr, "AREA OF ");
    return addr;
  }

  // Township names used for out of county calls
  private static final String[] CITY_LIST = new String[] {
    
    // Columbia County
    "COLUMBIA CO",
    "COLUMBIA COUNTY",
    "BEAVER TWP",
    "BENTON TWP",
    "BRIAR CREEK TWP",
    "CATAWISSA TWP",
    "CLEVELAND TWP",
    "CONYNGHAM TWP",
    "FISHING CREEK TWP",
    "FRANKLIN TWP",
    "GREENWOOD TWP",
    "HEMLOCK TWP",
    "JACKSON TWP",
    "LOCUST TWP",
    "MADISON TWP",
    "MAIN TWP",
    "MIFFLIN TWP",
    "MONTOUR TWP",
    "MOUNT PLEASANT TWP",
    "NORTH CENTRE TWP",
    "ORANGE TWP",
    "PINE TWP",
    "ROARING CREEK TWP",
    "SCOTT TWP",
    "SOUTH CENTRE TWP",
    "SUGARLOAF TWP",

    // Luzerne County
    "LUZERNE CO",
    "LUZERNE COUNTY",
    "LUZ COUTY",
    "LUZ COUNTY",
    "LUZE CO",
    "BEAR CREEK TWP",
    "BLACK CREEK TWP",
    "BUCK TWP",
    "BULTER TWP",  // Misspelled
    "BUTLER TWP",
    "CONYNGHAM TWP",
    "DALLAS TWP",
    "DENNISON TWP",
    "DORRENCE TWP", // Misspelled
    "DORRANCE TWP",
    "EXETER TWP",
    "FAIRMOUNT TWP",
    "FAIRVIEW TWP",
    "FOSTER TWP",
    "FRANKLIN TWP",
    "HANOVER TWP",
    "HAZLE TWP",
    "HOLLENBACK TWP",
    "HUNLOCK TWP",
    "HUNTINGTON TWP",
    "MAHONING TWP",
    "NESCOPECK TWP",
    "ROSS TWP",
    "SALEM TWP",
    "FAIRMOUNT",
    "FAIRVIEW",
    "FOSTER",
    "FRANKLIN",
    "HANOVER",
    "HAZLE",
    "HAZLETON",
    "HAZLETON CITY",
    "HOLLENBACK",
    "HUNLOCK",
    "HUNTINGTON",
    "JACKSON",
    "JENKINS",
    "KINGSTON",
    "LAKE",
    "LEHMAN",
    "MAHONING",
    "NESCOPECK",
    "NEWPORT",
    "PITTSTON",
    "PLAINS",
    "PLYMOUTH",
    "RICE",
    "ROSS",
    "SALEM",
    "SLOCUM",
    "SUGARLOAF",
    "UNION",
    "WILKES-BARRE",
    "WRIGHT",
    
    // Lycoming County
    "LYCOMING CO",
    "LYCOMING COUNTY",
    "ANTHONY TWP",
    "ARMSTRONG TWP",
    "BASTRESS TWP",
    "BRADY TWP",
    "BROWN TWP",
    "CASCADE TWP",
    "CLINTON TWP",
    "COGAN HOUSE TWP",
    "CUMMINGS TWP",
    "ELDRED TWP",
    "FAIRFIELD TWP",
    "FRANKLIN TWP",
    "GAMBLE TWP",
    "HEPBURN TWP",
    "JACKSON TWP",
    "JORDAN TWP",
    "LEWIS TWP",
    "LIMESTONE TWP",
    "LOYALSOCK TWP",
    "LYCOMING TWP",
    "MCHENRY TWP",
    "MCINTYRE TWP",
    "MCNETT TWP",
    "MIFFLIN TWP",
    "MILL CREEK TWP",
    "MORELAND TWP",
    "MUNCY TWP",
    "MUNCY CREEK TWP",
    "NIPPENOSE TWP",
    "OLD LYCOMING TWP",
    "PENN TWP",
    "PIATT TWP",
    "PINE TWP",
    "PLUNKETTS CREEK TWP",
    "PORTER TWP",
    "SHREWSBURY TWP",
    "SUSQUEHANNA TWP",
    "UPPER FAIRFIELD TWP",
    "WASHINGTON TWP",
    "WATSON TWP",
    "WOLF TWP",
    "WOODWARD TWP",
    
    // Montour County
    "MONTOUR CO",
    "MONTOUR COUNTY",
    "ANTHONY TWP",
    "COOPER TWP",
    "DERRY TWP",
    "LIBERTY TWP",
    "LIMESTONE TWP",
    "MAHONING TWP",
    "MAYBERRY TWP",
    "VALLEY TWP",
    "WEST HEMLOCK TWP",
    "DANVILLE",
    
    // Northumberland County
    "NORTHUMBERLAND CO",
    "NORTHUMBERLAND COUNTY",
    "COAL TWP",
    "DELAWARE TWP",
    "EAST CAMERON TWP",
    "EAST CHILLISQUAQUE TWP",
    "JACKSON TWP",
    "JORDAN TWP",
    "LEWIS TWP",
    "LITTLE MAHANOY TWP",
    "LOWER AUGUSTA TWP",
    "LOWER MAHANOY TWP",
    "MOUNT CARMEL TWP",
    "POINT TWP",
    "POTTSGROVE",
    "PT TOWNSHIP",
    "PT TWP",
    "RALPHO TWP",
    "ROCKEFELLER TWP",
    "RUSH TWP",
    "SHAMOKIN TWP",
    "TURBOT TWP",
    "UPPER AUGUSTA TWP",
    "UPPER MAHANOY TWP",
    "WASHINGTON TWP",
    "WEST CAMERON TWP",
    "WEST CHILLISQUAQUE TWP",
    "ZERBE TWP",
    "RALPHO",
    "RIVERSIDE",
    "SHAMOKIN",
    "SUNBURY",
    
    // Scuylkill County
    "SCUYLKILL CO",
    "SCUYLKILL COUNTY",
    "BARRY TWP",
    "BLYTHE TWP",
    "BRANCH TWP",
    "BUTLER TWP",
    "CASS TWP",
    "DELANO TWP",
    "EAST BRUNSWICK TWP",
    "EAST NORWEGIAN TWP",
    "EAST UNION TWP",
    "ELDRED TWP",
    "FOSTER TWP",
    "FRAILEY TWP",
    "HEGINS TWP",
    "HUBLEY TWP",
    "KLINE TWP",
    "MAHANOY TWP",
    "NEW CASTLE TWP",
    "NORTH MANHEIM TWP",
    "NORTH UNION TWP",
    "NORWEGIAN TWP",
    "PINE GROVE TWP",
    "PORTER TWP",
    "REILLY TWP",
    "RUSH TWP",
    "RYAN TWP",
    "SCHUYLKILL TWP",
    "SOUTH MANHEIM TWP",
    "TREMONT TWP",
    "UNION TWP",
    "UPPER MAHANTONGO TWP",
    "WALKER TWP",
    "WASHINGTON TWP",
    "WAYNE TWP",
    "WEST BRUNSWICK TWP",
    "WEST MAHANOY TWP",
    "WEST PENN TWP",
 
    // Sulivan County
    "SULIVAN CO",
    "SULIVAN COUNTY",
    "CHERRY TWP",
    "COLLEY TWP",
    "DAVIDSON TWP",
    "ELKLAND TWP",
    "FORKS TWP",
    "FOX TWP",
    "HILLSGROVE TWP",
    "LAPORTE TWP",
    "SHREWSBURY TWP"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANTHONY",        "ANTHONY TWP",
      "BEAVER",         "BEAVER TWP",
      "BLACKCREEK TWP", "BLACK CREEK TWP",
      "BLOOM",          "BLOOMSBURG",
      "BR CRK B",       "BRIAR CREEK",
      "BR CRK T",       "BRIAR CREEK TWP",
      "BULTER TWP",     "BUTLER TWP",
      "CATA B",         "CATAWISSA",
      "CATA T",         "CATAWISSA TWP",
      "CLEV",           "CLEVELAND TWP",
      "CLEVELAN",       "CLEVELAND TWP",
      "CLEVELAND",      "CLEVELAND TWP",
      "COOPER",         "COOPER TWP",
      "DAVIDSON",       "DAVIDSON TWP",
      "DERRENCE TWP",   "DERRANCE TWP",
      "DERRY",          "DERRY TWP",
      "DERRRY TO",      "DERRY TWP",
      "FAIRMONT",       "FAIRMOUNT TWP",
      "FAIRMONT TWP",   "FAIRMOUNT TWP",
      "FAIRMOUNT",      "FAIRMOUNT TWP",
      "FRANKLIN",       "FRANKLIN TWP",
      "FISH CRK",       "FISHING CREEK TWP",
      "GRW TWP",        "GREENWOOD TWP",
      "HEMLOCK",        "HEMLOCK TWP",
      "HUNINGTON TWP",  "HUNTINGTON TWP",
      "HOLLENBACH TWP", "HOLLENBACK TWP",
      "JACKSON",        "JACKSON TWP",
      "LIBERTY",        "LIBERTY TWP",
      "LIMESTONE",      "LIMESTONE TWP",
      "LOCUST",         "LOCUST TWP",
      "LUZ COUNTY",     "LUZERNE COUNTY",
      "LUZ COUTY",      "LUZERNE COUNTY",
      "MADISON",        "MADISON TWP",
      "MAHONING",       "MAHONING TWP",
      "MAIN",           "MAIN TWP",
      "MIFFLIN",        "MIFFLIN TWP",
      "MT PLEAS",       "MT PLEASANT TWP",
      "N CENTRE",       "N CENTRE TWP",
      "NESCOTWP",       "NESCOPECK TWP",
      "OVILLE B",       "ORANGEVILLE",
      "PT TOWNSHIP",    "POINT TWP",
      "PT TWP",         "POINT TWP",
      "PINE",           "PINE TWP",
      "ROAR CRK",       "ROARING CREEK TWP",
      "RUSH",           "RUSH TWP",
      "S CENTRE",       "S CENTRE TWP",
      "SCOTT",          "SCOTT TWP",
      "SCOTT  TO",      "SCOTT TWP",
      "SUGARLOAF",      "SUGARLOAF TWP",
      "STILLWAT",       "STILLWATER",
      "W HEMLOCK",      "WEST HEMLOCK TWP",
      "WASHNGTON",      "WASHINGTON TWP",
      
      "FAIRMOUNT TWP LUZE CO",    "FAIRMOUNT TWP"

  });

}
