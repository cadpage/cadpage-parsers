package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class ORClackamasCountyDParser extends SmartAddressParser {

  public ORClackamasCountyDParser() {
    super("GRESHAM", "OR");
    setFieldList("CODE CALL TIME ADDR PLACE CITY MAP X CH UNIT ID SRC");
  }
  
  @Override
  public String getFilter() {
    return "HDP7@portlandoregon.gov";
  }
 
  private static final String
    TYPE_PATTERN_S
      = "([A-Z0-9]+)(?: +([A-Z0-9]+|- .+?|\\*[A-Z]))? +",
    LOCATION_PATTERN_S
      = "(\\d{2}:\\d{2}:\\d{2})(.*?)",
    MAP1_PATTERN_S
      = "Dt: ([A-Z]{2}) +",
    MAP2_PATTERN_S
      = "Zne: (\\d{4}) +",
    TRAILER_PATTERN_S
      = "Gd: (\\d{4}[A-Z])(.+?)/ BLOCK:<.+?>(.*?)RG #(\\d+) Sent by:(.*)";

  private static final Pattern MASTER_PATTERN
    = Pattern.compile(TYPE_PATTERN_S
                     +LOCATION_PATTERN_S
                     +MAP1_PATTERN_S
                     +MAP2_PATTERN_S
                     +TRAILER_PATTERN_S);

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher m = MASTER_PATTERN.matcher(body);
    if (m.matches()) {
      parseType(m.group(1), getOptGroup(m.group(2)).trim(), data);
      data.strTime = m.group(3);
      parseLocation(m.group(4).trim(), data);
      data.strMap = append(m.group(5), "-", append(m.group(6), "-", m.group(7)));
      parseCross(m.group(8).trim(), data);
      parseUnit(m.group(9).trim(), data);
      data.strCallId = m.group(10);
      data.strSource = m.group(11).trim();
      
      return true;
    }
    
    return false;
  }

  private void parseType(String field1, String field2, Data data) {
    if(field2.equals(""))
      data.strCode = field1;
    else
      if (field2.charAt(0) == '-') {
        data.strCode = field1;
        data.strCall = field2.substring(1).trim();
      }
      else
        if (field2.charAt(0) == '*')
          data.strCode = append(field1, " ", field2);
        else
          data.strCall = append(field1, " ", field2);
  }
  
  private void parseLocation(String field, Data data) {
    int b = field.indexOf('[', 0);
    if (b != -1) {
      data.strPlace = field.substring(b+1).trim();
      field = field.substring(0, b);
    }
    parseAddress(field, data);
  }
    
  private static final Pattern PLACE_PATTERN
    = Pattern.compile("[0-9\\-]+");
  private void parseCross(String field, Data data) {
      String placeSave = data.strPlace;
      data.strPlace = "";
      Result r = parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS, field);
      if (r.getStatus() == 3) {
        r.getData(data);
        Matcher m = PLACE_PATTERN.matcher(data.strPlace);
        if (m.matches()) {
          data.strCross = append(data.strPlace, " ", data.strCross);
          data.strPlace = placeSave;
        }
        else
          data.strPlace = append(placeSave, " - ", data.strPlace);
      }
      else
        data.strPlace = append(placeSave, " - ", field);
    }
    
    private static final Pattern UNIT_PATTERN
      = Pattern.compile("(?:Ch: ([A-Z0-9]+))?(.*)");
    private void parseUnit(String field, Data data) {
      Matcher m = UNIT_PATTERN.matcher(field);
      if (m.matches()) {
        data.strChannel = getOptGroup(m.group(1));
        data.strUnit = m.group(2).trim();
      }
    }
}
