package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Oldham County, KY
 */
public class KYOldhamCountyAParser extends DispatchEmergitechParser {
  
  public KYOldhamCountyAParser() {
    super("DISPATCH:", CITY_LIST, "OLDHAM COUNTY", "KY");
    addSpecialWords("COLTON");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@oldhamcounty.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equalsIgnoreCase("LAGRANGE")) data.strCity =  "LA GRANGE";
    
    // They use the unit field for a dispatcher ID, which we are not interested in
    data.strUnit = "";
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
    "BALLARDSVILLE",
    "BROWNSBORO",
    "BUCKNER",
    "CENTERFIELD",
    "CRESTWOOD",
    "FLOYDSBURG",
    "GOSHEN",
    "LAGRANGE",
    "LA GRANGE",
    "ORCHARD GRASS HILLS",
    "PARK LAKE",
    "PEWEE VALLEY",
    "PROSPECT",
    "RIVER BLUFF",
    "WESTPORT",
    
    // Jefferson County
    "LOUISVILLE",
    
    // Shelby County
    "SHELBYVILLE"
  };
}
