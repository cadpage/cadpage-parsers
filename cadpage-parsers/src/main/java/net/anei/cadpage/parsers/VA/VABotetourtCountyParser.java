package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;

/**
 * Botetourt County, VA
 */
public class VABotetourtCountyParser extends DispatchSouthernPlusParser {
  
  public VABotetourtCountyParser() {
    super(CITY_LIST, "BOTETOURT COUNTY", "VA", DSFLAG_OPT_DISPATCH_ID | DSFLAG_TRAIL_PLACE | DSFLAG_FOLLOW_CROSS);
    setupSpecialStreets("AVERY ROW", "LITTLE TIMBER RDG");
    removeWords("COURT", "PARKWAY", "PLACE", "RUN", "TER");
  }

  @Override
  public String getFilter() {
    return "@botetourtva.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCall = stripFieldEnd(data.strCall, "-");
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "BUCHANAN",
    "FINCASTLE",
    "TROUTVILLE",

    // Unincorporated Communities
    "ARCADIA",
    "BLUE RIDGE",
    "CLOVERDALE",
    "DALEVILLE",
    "EAGLE ROCK",
    "GLEN WILTON",
    "HOLLINS",
    "LITHIA",
    "NACE",
    "ORISKANY",
    "ROANOKE",
    "SPRINGWOOD",
    
    // Alleghany County
    "ALLEGHANY",
    "ALLEGHANY CO",
    "CLIFTON FORGE",
    
    // Bedford County
    "BEDFORD",
    "BEDFORD CO",
    "MONTVALE",
    "THAXTON",
    
    // Craig County,
    "CRAIG",
    "CRAIG CO",
    
    // Roanoke County
    "ROANOKE CO",
    "VINTON",
    
    // Rockbridge County
    "ROCKBRIDGE",
    "ROCKBRIDGE CO",
    "NATURAL BRIDGE STATION",
    
    // Independent cities
    "ROANOKE CITY"
  }; 
}
