package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class WVKanawhaCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("(?:Metro911:|Metro CAD Alert:)?(.+?) reported at (.+?)(?:@(.*))? in (.+?)(?:\\((.*?)\\))? on (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)(?: +Call (?:#|Number:) *(.*))?");
  
  public WVKanawhaCountyParser() {
    super("KANAWHA COUNTY", "WV");
    setFieldList("CALL ADDR CITY PLACE DATE TIME ID");
  }
  
  @Override
  public String getFilter() {
    return "@metro911.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END | FLAG_IMPLIED_INTERSECT, match.group(2).trim().replaceAll("//", "/"), data);
    data.strPlace = getOptGroup(match.group(3));
    data.strCity = match.group(4).trim();
    if (data.strCity.equals("SISSONVILLE")) {
      if (data.strPlace.length() == 0) data.strPlace = data.strCity;
      data.strCity = "CHARLESTON";
    }
    data.strPlace = append(data.strPlace, " - ", getOptGroup(match.group(5)));
    data.strDate = match.group(6);
    data.strTime = match.group(7);
    data.strCallId = getOptGroup(match.group(8));
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("LITTLE TYLER")) return "3";
    return city;
  }
}
