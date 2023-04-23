package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALPikeCountyAParser extends DispatchSouthernParser {
  
  public ALPikeCountyAParser() {
    super(CITY_LIST, "PIKE COUNTY", "AL", 
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_X | DSFLG_OPT_NAME | DSFLG_OPT_PHONE | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
    removeWords("PIKE");
  }
  
  @Override
  public String getFilter() {
    return "2183500392";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Pike County 9-1-1:");
    return super.parseMsg(body, data);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final String[] CITY_LIST = new String[] {
      
      // Cities
      "BRUNDIDGE",
      "TROY",
      
      // Towns
      "BANKS",
      "GOSHEN",
      
      // Unincorporated communities
      "CHINA GROVE",
      "CURRY",
      "HENDERSON",
      "JONESVILLE",
      "JOSIE",
      "KENT",
      "NEEDMORE",
      "ORION",
      "PRONTO",
      "SHADY GROVE",
      "SPRING HILL"
      
  };

}
