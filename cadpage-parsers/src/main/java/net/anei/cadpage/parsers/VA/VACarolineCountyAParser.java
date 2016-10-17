package net.anei.cadpage.parsers.VA;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class VACarolineCountyAParser extends MsgParser {
  
  public VACarolineCountyAParser() {
    super("CAROLINE COUNTY", "VA");
    setFieldList("UNIT DATE TIME ID CALL ADDR APT CITY INFO");
  }
  
  private static final Pattern MASTER = Pattern.compile("(?:superuser|ACTIVE 911-([A-Z0-9]+)):As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) (\\d{12}) ([A-Z0-9]+) ([^,]+)(?:, *([ A-Z]+))? (Call Created Time:.* \\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d)\\b *(.*)");
  private static final Pattern INFO_SPLIT_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d) +");
  private static final Pattern UNIT_SPLIT_PTN = Pattern.compile("[, ]+");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    Set<String> unitSet = new HashSet<String>();
    
    String unit = match.group(1);
    if (unit != null) addUnit(unit, unitSet, data);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    data.strCallId = match.group(4);
    data.strCall = match.group(5);
    parseAddress(match.group(6).trim(), data);
    data.strCity = splitCity(getOptGroup(match.group(7)));
    data.strSupp = INFO_SPLIT_PTN.matcher(match.group(8).trim()).replaceAll("\n");
    unit = match.group(9);
    for (String unt : UNIT_SPLIT_PTN.split(unit)) {
      addUnit(unt, unitSet, data);
    }
    return true;
  }
  
  private void addUnit(String unit, Set<String> unitSet, Data data) {
    unit = unit.toUpperCase();
    if (unitSet.add(unit)) {
      data.strUnit = append(data.strUnit, ",", unit);
    }
  }
  
  private String splitCity(String city) {
    if (city.length() == 0) return city;
    int pt =  city.length()/2;
    if (city.charAt(pt) == ' ') {
      String tmp1 = city.substring(0,pt);
      String tmp2 = city.substring(pt+1);
      if (tmp1.equals(tmp2)) city = tmp1;
    }
    return city;
  }
}