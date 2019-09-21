package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyLParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyLParser() {
    super("ADDR! XST:X! MUN:CITY! NAT:CALL! CALL/S+ MAP/BOX:BOX! BOX/D+ ADC:MAP! ID! TIME:TIME! NOTES:INFO! INFO/N+ TRUCKS:UNIT! UNIT/C+");
  }
  
  private static final Pattern TIME_PREFIX_PTN = Pattern.compile("(\\d\\d:\\d\\d) {2,}");
  private static final Pattern MASTER = 
      Pattern.compile("(.+?)  (.*)  (\\d\\d/\\d\\d/\\d\\d)  ([A-Z]{1,4}\\d{8})  ([A-Z0-9,]+)  ([A-Z]+) {2,3}(\\d\\d[A-Z]?|OO)(?:  +(.*?))?(?:  +([A-Z]+\\d+))?");
  private static final Pattern DELIM = Pattern.compile("[, ]*\n| +(?=NAT:|TRUCKS:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = TIME_PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strTime = match.group(1);
      body = body.substring(match.end());
      int pt = body.indexOf("  ");
      if (pt < 0) return false;
      
      setFieldList("TIME CALL ADDR APT PLACE DATE ID MAP CITY BOX X UNIT");
      data.strCall = body.substring(0, pt).trim().replace('\n', '-');
      body = body.substring(pt+2).trim().replace("\n", "");

      match = MASTER.matcher(body);
      if (!match.matches()) return false;
      
      parseAddress(match.group(1).trim(), data);
      data.strPlace = stripFieldStart(match.group(2).trim(), "-");
      data.strDate = match.group(3);
      data.strCallId = match.group(4);
      data.strMap = match.group(5);
      data.strCity = convertCityCode(match.group(6));
      data.strBox = match.group(7);
      data.strCross = getOptGroup(match.group(8));
      data.strUnit = getOptGroup(match.group(9));
      return true;
    }
    
    body = body.replace(",I#", "\nID:");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": @");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile("([-+]\\d{3}\\.\\d{6} [-+]\\d{3}\\.\\d{6})\\b *(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1), data);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }
} 
