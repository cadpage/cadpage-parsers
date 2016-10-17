package net.anei.cadpage.parsers.ME;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class MEKnoxCountyParser extends SmartAddressParser {
  
  public MEKnoxCountyParser() {
    super(CITY_LIST, "KNOX COUNTY", "ME");
    setFieldList("CALL ADDR APT CITY INFO");
  }
  
  @Override
  public String getFilter() {
    return "@knoxcountymaine.gov";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("KNOX COUNTY (ME)"))  return false;
    parseAddress(StartType.START_CALL, FLAG_NO_IMPLIED_APT, body, data);
    if (!isValidAddress()) return false;
    String left = getLeft();
    if (left.startsWith("/")) left = left.substring(1).trim();
    if (data.strCall.length() == 0) {
      data.strCall = left;
    } else {
      data.strSupp =  left;
    }
    return true;
  }
 
  private static final String[] CITY_LIST = new String[]{
    "APPLETON",
    "CAMDEN",
    "CUSHING",
    "FRIENDSHIP",
    "HOPE",
    "ISLE AU HAUT",
    "MATINICUS ISLE",
    "NORTH HAVEN",
    "OWLS HEAD",
    "ROCKLAND",
    "ROCKPORT",
    "SAINT GEORGE",
    "SOUTH THOMASTON",
    "TENANTS HARBOR",
    "THOMASTON",
    "UNION",
    "VINALHAVEN",
    "WARREN",
    "WASHINGTON"
  };
}
