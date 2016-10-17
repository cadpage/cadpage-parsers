package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Muhlenberg County, KY
 */
public class KYMuhlenbergCountyParser extends DispatchEmergitechParser {
  
  public KYMuhlenbergCountyParser() {
    super("CDF911:", 60, CITY_LIST, "MUHLENBERG COUNTY", "KY");
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Occasionally the extra pace goes in 66 instead of 67.
    // But that only seems to happen to one word, so we will fix it here
    body = body.replace(" B ETWEEN ", " BETWEEN ");
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{
    "BEECH CREEK",
    "BEECHMONT",
    "BELTON",
    "BREMEN",
    "BROWDER",
    "CENTRAL CITY",
    "DEPOY",
    "DRAKESBORO",
    "DUNMOR",
    "ENNIS",
    "GRAHAM",
    "GREENVILLE",
    "POWDERLY",
    "SOUTH CARROLLTON"
  };
}
