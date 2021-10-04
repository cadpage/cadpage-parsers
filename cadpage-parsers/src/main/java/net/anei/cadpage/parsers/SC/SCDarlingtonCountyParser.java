
package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class SCDarlingtonCountyParser extends DispatchSouthernParser {

  public SCDarlingtonCountyParser() {
    super(CITY_LIST, "DARLINGTON COUNTY", "SC",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_CODE | DSFLG_OPT_ID | DSFLG_TIME);
    removeWords("KNOLL", "SQUARE");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "CAD:");
    body = stripFieldStart(body, "2;");
    return super.parseMsg(body, data);
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("MM")) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] CITY_LIST = new String[]{
    "CLYDE",
    "DARLINGTON",
    "HARTSVILLE",
    "LAMAR",
    "NORTH HARTSVILLE",
    "SOCIETY HILL",

    // Charleston County
    "CHARLESTON",

    // Chesterfield County
    "CHESTERFIELD",
    "MCBEE",

    // Florence County
    "COWARD",
    "EFFINGHAM",
    "FLORENCE",
    "JOHNSONVILLE",
    "LAKE CITY",
    "PAMPLICO",
    "QUINBY",
    "SCRANTON",
    "TIMMONSVILLE",

    // Lee County
    "LEE COUNTY",
    "BISHOPVILLE"
  };
}
