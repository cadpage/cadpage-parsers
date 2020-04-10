package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStCharlesCountyParser extends FieldProgramParser {
  
  private static final Pattern ID_PTN = Pattern.compile("\\d{6}-\\d{5}");
 
  public MOStCharlesCountyParser() {
    super("ST CHARLES COUNTY", "MO",
          "ADDR! APT:APT! BUS:PLACE! FD:CITY! CHL:CH! GPS:GPS? Units:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@sccda.org,dispatch@sccmo.org,SCCEC_info@sccmo.org";
  }
  
  private static final Pattern PRENOTE_PTN = Pattern.compile("\\d+\\) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d)-\\[\\d+\\] (?:\\[Notification\\])? *(.*)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Dispatch sends a fixed length field format and a variable length field format.
    // Try the variable format first
    if (super.parseMsg(body, data)) return true;
    
    // Otherwise reset things and try the fixed field format
    data.initialize(this);
    
    FParser p = new FParser(body);
    
    // Check for special New Notification format
    if (p.check("New Notification:")) {
      setFieldList("CODE CALL ADDR APT INFO");
      parseCodeCall(p.get(26), data);
      parseAddress(p.get(31), data);
      if (p.getOptional(" [Notification] ", 26, 28) == null) return false;
      data.strSupp = p.get();
      return true;
    }
    
    // Skip optional ID: label
    p.check("ID:");
    
    data.strCallId = p.get(12);
    if (! ID_PTN.matcher(data.strCallId).matches()) return false;
    
    if (p.check("        New Notification:")) {
      setFieldList("ID CODE CALL ADDR DATE TIME INFO");
      parseCodeCall(p.get(26), data);
      parseAddress(p.get(31), data);
      String info = p.get();
      Matcher match = PRENOTE_PTN.matcher(info);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strSupp = match.group(3);
      return true;
    }
    
    setFieldList("ID INFO CODE CALL ADDR PLACE APT X MAP CH CITY GPS UNIT");

    data.strSupp = p.get(8);
    int fLen = p.checkAhead("APT:", 101, 100);
    if (fLen < 0) return false;
    fLen -= 75; 
    parseCodeCall(p.get(fLen), data);
    parseAddress(p.get(50), data);
    data.strPlace = p.get(25);
    if (!p.check("APT:")) return false;
    
    p.setOptional();
    data.strApt = p.getOptional("X ST:", 5, 16);
    if (data.strApt == null) return false;
    data.strCross = p.get(30);
    
    if (!p.check("MAP:")) return false;
    String map = p.getOptional("CHL:", 10);
    if (map != null) {
      data.strMap = map;
      fLen = p.checkAhead("Units:", 35, 39);
      if (fLen < 0) fLen = p.checkAhead("GPS:", 35, 39);
      if (fLen < 0) return false;
      data.strChannel = p.get(fLen-30);
      data.strCity = p.get(30);
      if (data.strCity.equalsIgnoreCase("Unincorporated")) data.strCity = "";
      if (p.check("GPS:")) {
        p.check(" ");
        String gps = p.get(2)+'.'+p.get(6);
        if (!p.checkBlanks(2)) return false;
        p.check(" ");
        setGPSLoc(gps+','+p.get(3)+'.'+p.get(6), data);
        if (!p.checkBlanks(2)) return false;
      }
      if (!p.check("Units:")) return false;
      data.strUnit = p.get();
      return true;
      
    } else {
      data.strMap = p.get(15);
      data.strCity = p.get(30);
      if (data.strCity.equalsIgnoreCase("Unincorporated")) data.strCity = "";
      if (!p.check("Units:")) return false;
      data.strUnit = p.get();
      return true;
    }
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?|\\d{2}) *(.*)");
  
  private void parseCodeCall(String call, Data data) {
    Matcher match = CODE_CALL_PTN.matcher(call);
    if (match.matches()) {
      data.strCode = match.group(1);
      call = match.group(2);
    }
    data.strCall = call;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_PTN = Pattern.compile("(?:Comment: *(.*), {2,})?(?:ID: *(\\d{6}-\\d{5}) +)?(?:(\\d\\d?[A-Z]\\d\\d?[A-Z]?) +)?(.*) AT (.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSupp = getOptGroup(match.group(1));
      data.strCallId = getOptGroup(match.group(2));
      data.strCode = getOptGroup(match.group(3));
      data.strCall = match.group(4).trim();
      super.parse(match.group(5).trim(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO ID CODE CALL " + super.getFieldNames();
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("(\\d{2})(\\d{6}) - (\\d{2})(\\d{6})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+'.'+match.group(2)+",-"+match.group(3)+'.'+match.group(4), data);
      }
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    String tmp = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (tmp != null) city = tmp;
    return super.adjustMapCity(city);
  }
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "CENTRAL COUNTY",     "St Peters",
      "DISPATCH.",          "",
      "RIVERS POINTE",      ""
  });
}