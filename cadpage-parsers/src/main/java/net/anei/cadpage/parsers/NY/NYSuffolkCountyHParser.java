package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class NYSuffolkCountyHParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d) (.*?) (AM|EH|MO|SP) (.*)");
  
  public NYSuffolkCountyHParser() {
    super(CITY_LIST, "SUFFOLK COUNTY", "NY");
    setFieldList("DATE TIME CALL CITY ADDR APT PLACE INFO");
    setupSpecialStreets(
        "BEACHWAY",
        "CAPTAINS WALK",
        "EDWARDS CLOSE",
        "GLENWAY",
        "GREENWAY",
        "HIGHWOOD",
        "QUALITY ROW",
        "SHORIDGE",
        "WATERS EDGE",
        "WATERSEDGE", 
        "WINDWARD"
    );
  }
  
  @Override
  public String getFilter() {
    return "@easthamptonvillageny.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CALL")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    data.strCall = match.group(3).trim();
    String code = match.group(4);
    String addr = match.group(5).trim();
    parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS | FLAG_IGNORE_AT, addr, data);
    data.strSupp = getLeft();
    data.strPlace = data.strCity;
    data.strCity = convertCodes(code, CITY_CODES);
    if (data.strPlace.equals(data.strCity)) data.strPlace = "";
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AM", "AMAGANSETT",
      "EH", "EAST HAMPTON",
      "MO", "MONTAUK",
      "SP", "SPRINGS AMGANSETT"
  });
  
  private static final String[] CITY_LIST = new String[]{
    "AMAGANSETT",
    "EAST HAMPTON",
    "BARNES LANDING BERGER",
    "NAPEAGUE",
    "SPRINGS AMAM"
  };
}
