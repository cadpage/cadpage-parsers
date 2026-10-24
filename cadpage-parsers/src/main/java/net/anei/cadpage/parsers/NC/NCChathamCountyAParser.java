

package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Chattam County, NC
 */
public class NCChathamCountyAParser extends DispatchSouthernParser {

  public NCChathamCountyAParser() {
    super(CITY_LIST, "CHATHAM COUNTY", "NC",
          DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE|DSFLG_OPT_BAD_PLACE|DSFLG_OPT_CODE|DSFLG_ID|DSFLG_TIME|DSFLG_PROC_EMPTY_FLDS);
    removeWords("PLACE");
  }

  @Override
  public String getFilter() {
    return "@chathamnc.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("`", "");
    if (!super.parseMsg(body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, "NC");
    if (data.strCity.equalsIgnoreCase("RAMSUER")
        || data.strCity.equalsIgnoreCase("RAMSUERR")) data.strCity = "RAMSEUR";
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
    "CARY",
    "FEARRINGTON",
    "GOLDSTON",
    "PITTSBORO",
    "SILER CITY",
    "CHAPEL HILL",

    "BEAR CREEK",
    "BENNETT",
    "BONLEE",
    "BRICKHAVEN",
    "BYNUM",
    "CARBONTON",
    "CORINTH",
    "CRUTCHFIELD CROSSROADS",
    "GULF",
    "HAYWOOD",
    "MONCURE",
    "SILK HOPE",
    "WILSONVILLE",

    // Alamance County
    "GRAHAM",
    "SNOW CAMP",

    // Chatham County
    "PITTSBORO",

    // Durham County
    "DURHAM",

    // Lee County
    "SANFORD",

    // Mecklenburg County
    "MATTHEWS",

    // Moore County
    "CARTHAGE",
    "ROBBINS",

    // Orange County
    "ORANGE CO",
    "CARRBORO",

    // Randolph County
    "LIBERTY",
    "RAMSEUR",
    "RAMSUER",    // Misspelled
    "RAMSUERRNC", // Badly misspelled
    "SEAGROVE",
    "STALEY",

    // Wake County
    "APEX",
    "FUQUAY",
    "FUQUAY VARINA",
    "FUQUAY-VARINA",
    "HOLLY SPRINGS",
    "NEW HILL"
  };
}
