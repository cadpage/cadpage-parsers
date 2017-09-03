package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyLParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyLParser() {
    super("ADDR! XST:X! MUN:CITY! NAT:CALL! MAP/BOX:BOX! ADC:MAP! ID! TIME:TIME! NOTES:INFO! INFO/CS+ TRUCKS:UNIT! UNIT/C+");
  }
  
  private static final Pattern MASTER = 
      Pattern.compile("(\\d\\d:\\d\\d)  +([^\\*]+?)(?: \\*)?  ([^,]+?(?:,[ A-Z0-9]+?)?)  (.+)  (\\d\\d/\\d\\d/\\d\\d)  ([A-Z]{1,4}\\d{8})  +([A-Z]+)(?:  +(.*))?");
  private static final Pattern DELIM = Pattern.compile(", *| +(?=XST:|MUN:|NAT:|TRUCKS:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Trappe")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("TIME CALL ADDR APT PLACE DATE ID CITY X");
      data.strTime = match.group(1);
      data.strCall = match.group(2).trim();
      parseAddress(match.group(3).trim(), data);
      data.strPlace = stripFieldStart(match.group(4).trim(), "-");
      data.strDate = match.group(5);
      data.strCallId = match.group(6);
      data.strCity = convertCityCode(match.group(7));
      data.strCross = getOptGroup(match.group(8));
      return true;
    }
    
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
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
