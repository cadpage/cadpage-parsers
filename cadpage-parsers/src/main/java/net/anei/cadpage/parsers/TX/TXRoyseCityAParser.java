
package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TXRoyseCityAParser extends DispatchSouthernParser {

  public TXRoyseCityAParser() {
    super(CITY_LIST, "ROYSE CITY", "TX", 
          DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE2|DSFLG_OPT_X|DSFLG_ID|DSFLG_TIME);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CFS Closed:")) {
      data.msgType = MsgType.RUN_REPORT;
      body = body.substring(11).trim();
    }
    return super.parseMsg(body, data);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BARBERSHOP S ELM",
      "BLACK HAWK",
      "BUENA VISTA",
      "COUNTY LINE",
      "ELM GROVE",
      "ERBY CAMPBELL",
      "GRANITE RIDGE",
      "HARVEST RIDGE",
      "HIGH BLUFF",
      "JOE BAILEY",
      "LAKE ESTATES",
      "LOST SPUR",
      "OAK GROVE",
      "PHEASANT HILL",
      "QUAIL CREEK",
      "RANCH HOUSE",
      "RUSTIC MEADOW",
      "SANTA FE",
      "SERENE HAVEN",
      "SHOOTING STAR",
      "STERLING RIDGE",
      "STONE RIVER",
      "VIA TOSCANA",
      "WINDING CREEK"
  };
  
  private static final String[] CITY_LIST = new String[]{
    "DALLAS",
    "FATE",
    "HEATH",
    "MCLENDON CHISHOLM",
    "MCLENDON-CHISHOLM",
    "MOBILE CITY",
    "NEVADA",
    "ROCKWALL",
    "ROWLETT",
    "ROYSE CITY",
    "WYLIE",

    "COLLIN CO",
    "COLLIN COUNTY",
    "HUNT CO",
    "HUNT COUNTY",
    "ROCKWALL CO",
    "ROCKWALL COUNTY"
  };
}
