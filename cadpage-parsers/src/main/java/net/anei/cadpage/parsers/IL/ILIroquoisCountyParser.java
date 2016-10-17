package net.anei.cadpage.parsers.IL;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class ILIroquoisCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER1 = Pattern.compile("(.*?)(?:[/,]| - )(.*)");
  private static final Pattern MASTER2 = Pattern.compile("(.*?),(.*?)(?:[/,]| - )(.*)");
  private static final Pattern LEAD_NUMBER_PTN = Pattern.compile("\\d+ .*");
  
  public ILIroquoisCountyParser() {
    super(CITY_LIST, "IROQUOIS COUNTY", "IL");
    setFieldList("ADDR APT CITY CALL");
  }
  
  @Override
  public String getFilter() {
    return "@co.iroquois.il.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      String addr = match.group(1).trim();
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
      if (data.strCity.length() > 0) {
        data.strCall = match.group(2).trim();
        return true;
      }
      data.initialize(this);
    }
    
    match = MASTER2.matcher(body);
    if (match.matches()) {
      String city = match.group(2).trim();
      if (isCity(city)) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = city;
        data.strCall = match.group(3).trim();
        return true;
      }
    }
    
    if (!isPositiveId()) return false;
    parseAddress(StartType.START_ADDR, body, data);
    if (data.strCity.length() == 0) return false;
    if (!LEAD_NUMBER_PTN.matcher(data.strAddress).matches()) return false;
    data.strCall = getLeft();
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "GILMAN",
    "WATSEKA",

    // Villages
    "ASHKUM",
    "BEAVERVILLE",
    "BUCKLEY",
    "CHEBANSE",
    "CISSNA PARK",
    "CLIFTON",
    "CRESCENT CITY",
    "DANFORTH",
    "DONOVAN",
    "IROQUOIS",
    "LODA",
    "MARTINTON",
    "MILFORD",
    "ONARGA",
    "PAPINEAU",
    "SHELDON",
    "STOCKLAND",
    "THAWVILLE",
    "WELLINGTON",
    "WOODLAND",

    // Townships
    "ASH GROVE TWP",
    "BEAVER TWP",
    "BELMONT TWP",
    "CHEBANSE TWP",
    "CONCORD TWP",
    "LODA TWP",
    "MIDDLEPORT TWP",
    "MILFORD TWP",
    "ONARGA TWP",
    "PAPINEAU TWP",
    "STOCKLAND TWP",
    "MARTINTON TWP",
    "IROQUOIS TWP",
    "PRAIRIE GREEN TWP",
    "ASHKUM TWP",
    "DOUGLAS TWP",
    "ARTESIA TWP",
    "FOUNTAIN CREEK TWP",
    "LOVEJOY TWP",
    "SHELDON TWP",
    "MILKS GROVE TWP",
    "PIGEON GROVE TWP",
    "CRESCENT TWP",
    "DANFORTH TWP",
    "RIDGELAND TWP",
    "BEAVERVILLE TWP"

    
  };
}
