package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALPikeCountyParser extends DispatchSouthernParser {
  
  public ALPikeCountyParser() {
    super(CITY_LIST, "PIKE COUNTY", "AL", 
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE2 | DSFLG_ID | DSFLG_TIME);
  }
  
  @Override
  public String getFilter() {
    return "2183500392";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Pike County 9-1-1: ")) return false;
    body = body.substring(19).trim();
    return super.parseMsg(body, data);
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "BRUNDIDGE",
    "TROY",

    // Towns
    "BANKS",
    "GOSHEN",

    // Unincorporated communities
    "CURRY",
    "JONESVILLE",
    "KENT",
    "ORION",
    "PRONTO",
    "RIVER RIDGE",
    "SPRING HILL",
    "CHINA GROVE"
  };

}
