
package net.anei.cadpage.parsers.SC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class SCFairfieldCountyAParser extends DispatchSouthernParser {

  public SCFairfieldCountyAParser() {
    super(CITY_LIST, "FAIRFIELD COUNTY", "SC",
        DSFLG_ADDR|DSFLG_ADDR_LEAD_PLACE|DSFLG_OPT_X|DSFLG_OPT_UNIT1|DSFLG_ID|DSFLG_TIME);
    setupMultiWordStreets(
        "ASHFORD FERRY",
        "CAMP WELFARE",
        "CEDAR LAKES",
        "CLARK BRIDGE",
        "COLE TRESTLE",
        "FLINT HILL",
        "FOREST HILL",
        "GLENNS BRIDGE",
        "GOLF COURSE",
        "GREENBRIER MOSSYDALE",
        "JACKSON CREEK",
        "JANICE REEVES",
        "KEY HOLE",
        "LAMPLIGHTER APT",
        "MAGGIE HARRIS",
        "MOOD HARRISON",
        "PEAY RIDGE",
        "RIDING RIDGE",
        "ROLLING HILLS",
        "STATE PARK",
        "TRADE MILL",
        "WATEREE ESTATES",
        "WINNSBORO ARMS"
    );
    allowBadChars("()");
  }

  private static final Pattern MILE_MARKER_PTN = Pattern.compile("\\d+ MILE MARKER \\d+");
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    data.strAddress = data.strAddress.replace("INTERSTATE", "I");
    
    if (MILE_MARKER_PTN.matcher(data.strPlace).matches()) {
      data.strAddress = append(data.strPlace, " ", data.strAddress);
      data.strPlace = "";
    }
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    
    // There are 2 Alexander Cir streets, which dispatch refers to as\
    // ALEXANDER 1 CIR and ALEXANDER 2 CIR, but Google knows them both 
    // as ALEXANDER CIR
    addr = STREET_X_PTN.matcher(addr).replaceAll("$1");
    
    return super.adjustMapAddress(addr);
  }
  private static final Pattern STREET_X_PTN = Pattern.compile("\\b(ALEXANDER|CASTLEWOOD) *[12]\\b");///

  private static final String[] CITY_LIST = new String[]{
      
      "BLACKSTOCK",
      "BLAIR",
      "BUCKLICK",
      "JENKINSVILLE",
      "MITFORD",
      "MONTICELLO",
      "RIDGEWAY",
      "WINNSBORO",
      "WINNSBORO MILLS",
      
      // Chester County
      "GREATFALLS",
      "GREAT FALLS"
      
  };
}
