package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAColumbiaCountyBParser extends FieldProgramParser {
  
  public PAColumbiaCountyBParser() {
    super(CITY_CODES, "COLUMBIA COUNTY", "PA", 
          "( DATETIME1! Inc_Addr:ADDRCITY/S! Apt:APT! Cross_Streets:X1! GPS! Inc_Code:CALL! SubType:CALL/SDS! Dispatch_Time:DATETIME Responding_Unit(s):EMPTY! UNIT! CFS#:ID! DR_ID+? " + 
          "| DR_ID+? ADDRCITY/S GPS CALL! Disp_Time:DATETIME! Responding_Unit(s):EMPTY! UNIT! ) Comments:INFO/N+");
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
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Page for CFS (\\d{6}-\\d{1,3})");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new SkipField("[A-Z][a-z]{2} [A-Z][a-z]{2} \\d\\d \\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X1")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("DR_ID")) return new SkipField("DR#:.*", true);
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
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)\\((.*?)\\)");
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
      data.strApt = append(apt, "-", data.strApt);
      
      data.strCity = stripFieldEnd(data.strCity, " B");
      if (data.strCity.endsWith(" T")) data.strCity += "WP";
      
      if (data.strCity.length() == 0) {
        match = ADDR_CITY_PTN.matcher(data.strAddress);
        if (match.matches()) {
          data.strAddress = match.group(1).trim();
          data.strCity =  match.group(2).trim();
        }
      }
    }
    
    @Override
    public void setNoCity(boolean noCity) {}
    
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
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[\\d\\d:\\d\\d:\\d\\d [A-Za-z ]+\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_JUNK_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

  // Township names used for out of county calls
  private static final String[] CITY_LIST = new String[] {
    
    // Columbia County
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
    "BEAR CREEK TWP",
    "BLACK CREEK TWP",
    "BUCK TWP",
    "BUTLER TWP",
    "CONYNGHAM TWP",
    "DALLAS TWP",
    "DENNISON TWP",
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
    "FAIRMOUNT",
    "FAIRVIEW",
    "FOSTER",
    "FRANKLIN",
    "HANOVER",
    "HAZLE",
    "HOLLENBACK",
    "HUNLOCK",
    "HUNTINGTON",
    "JACKSON",
    "JENKINS",
    "KINGSTON",
    "LAKE",
    "LEHMAN",
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
    "ANTHONY TWP",
    "COOPER TWP",
    "DERRY TWP",
    "LIBERTY TWP",
    "LIMESTONE TWP",
    "MAHONING TWP",
    "MAYBERRY TWP",
    "VALLEY TWP",
    "WEST HEMLOCK TWP",
    
    // Northumberland County
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
    
    // Scuylkill County
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
      "GRW TWP",       "GREENWOOD TWP",
      "NESCOTWP",      "NESCOPECK TWP",
      "HUNINGTON TWP", "HUNTINGTON TWP"
  });

}
