package net.anei.cadpage.parsers.MO;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class MONewtonCountyParser extends FieldProgramParser {
 
  public MONewtonCountyParser() {
    super(CITY_LIST, "NEWTON COUNTY", "MO",
          "ADDR/S3XP! CrossStreets:X! ESN:MAP Call_Number:ID");
    removeWords("CIRCLE", "LANE");
  }
  
  @Override
  public String getFilter() {
    return "911@NC-CDC.ORG";
  }
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Call Number: *(\\d+) (.*?) (Call Received Time:.*)", Pattern.DOTALL);
  private static final Pattern TIMES_BRK_PTN = Pattern.compile("\\s+(?=[A-Z]+:)");
  
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\n", "");
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID ADDR APT CITY CALL INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      String addrCall = match.group(2).trim();
      String times = match.group(3).trim();
      parseAddress(StartType.START_ADDR, addrCall, data);
      data.strCall = getLeft();
      data.strSupp = TIMES_BRK_PTN.matcher(times).replaceAll("\n");
      return true;
    }
    return super.parseMsg(body, data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String addr = p.get(", Apt.");
      String apt = p.get(' ');
      if (apt.equalsIgnoreCase("LOT")) apt = apt + " " + p.get(' ');
      if (apt.length() > 0) {
        String extra = p.get();
        parseAddress(addr, data);
        data.strApt = apt;
        
        // There isn't really a following  cross street.  But there are place names
        // that look enough like cross streets that this will fix
        parseAddress(StartType.START_PLACE, FLAG_ONLY_CITY | FLAG_CROSS_FOLLOWS, extra, data);
        if (data.strCity.length() > 0) {
          data.strApt = append(data.strApt, " ", data.strPlace);
          data.strPlace = getLeft();
        }
      } else {
        super.parse(field, data);
        if (append(data.strApt, " ", data.strPlace).startsWith(data.strAddress)) {
          data.strApt = "";
          data.strPlace = "";
        }
      }
      String state = CITY_STATE_TABLE.getProperty(data.strCity);
      if (state != null) data.strState = state;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private static final Pattern MILE_PTN = Pattern.compile(" mi [NSEW][NSEW]? ");
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      int pt = 0;
      Matcher match = MILE_PTN.matcher(field);
      while (match.find()) pt = match.end();
      if (pt >= 0) {
        data.strCall = field.substring(pt).trim();
        data.strCross = field.substring(0,pt).trim();
      } else {
        Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, field);
        if (res.isValid()) {
          res.getData(data);
          data.strCall = res.getLeft();
        } else {
          data.strCall = field;
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X CALL";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    
      "NEWTON COUNTY",
    
      // Cities
      "DIAMOND",
      "FAIRVIEW",
      "GRANBY",
      "JOPLIN",
      "NEOSHO",
      "SENECA",
      
      // Villages
      "CLIFF VILLAGE",
      "DENNIS ACRES",
      "GRAND FALLS PLAZA",
      "LEAWOOD",
      "LOMA LINDA",
      "NEWTONIA",
      "REDINGS MILL",
      "RITCHEY",
      "SAGINAW",
      "SHOAL CREEK DRIVE",
      "SHOAL CREEK ESTATES",
      "SILVER CREEK",
      "STARK CITY",
      "STELLA",
      "WENTWORTH",
      
      // Other localities
      "HORNET",
      "MONARK SPRINGS",
      "RACINE",
      "SPRING CITY",
      "TIPTON FORD",
      "WANDA",
      
      // Townships
      "NEOSHO",
      "SHOAL CREEK",
      "GRANBY",
      "FIVE MILE",
      "MARION",
      "SENECA",
      "WEST BENTON",
      "BUFFALO",
      "FRANKLIN",
      "DAYTON",
      "VAN BUREN",
      "NEWTONIA",
      "BENTON",
      "BERWICK",
      
      // Barry County
      "MONETT",
      "WHEATON",
      
      // Jasper County
      "DUENWEG",
      
      // Lawrence County
      "PIERCE CITY",
      
      // Mcdonald County
      "ANDERSON",
      "GOODMAN",
      "PINEVILLE",
      
      // Ottawa County, OK
      "WYANDOTTE"
    
  };
  
  private static final Properties CITY_STATE_TABLE = buildCodeTable(new String[]{
      "WYADOTTE",    "OK"
  });
}