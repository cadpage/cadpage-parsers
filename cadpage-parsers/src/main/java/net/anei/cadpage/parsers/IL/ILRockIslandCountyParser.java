package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class ILRockIslandCountyParser extends DispatchOSSIParser {
  
  private static final Pattern MARKER = Pattern.compile("^\\d+:");
  
  public ILRockIslandCountyParser() {
    super(CITY_CODES, "ROCK ISLAND COUNTY", "IL",
          "ID:FYI ADDR CALL CITY!");
    setFieldList("SRC ID ADDR CALL CITY");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ricoetsb.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = match.group() + "CAD:" + body.substring(match.end()).trim();
    return super.parseMsg(body, data);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "EM",   "EAST MOLINE",
      "HIL",  "HILLSDALE",
      "MO",   "MOLINE",
      "PB",   "PORT BYRON",
      "RI",   "ROCK ISLAND",
      "RIA",  "ROCK ISLAND ARSENAL"
  });
  
}
