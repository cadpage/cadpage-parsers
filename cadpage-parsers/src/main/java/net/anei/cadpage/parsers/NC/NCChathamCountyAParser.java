

package net.anei.cadpage.parsers.NC;

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
    "ROBBINS",

    // Orange County
    "CARRBORO",

    // Randolph County
    "LIBERTY",
    "RAMSEUR",
    "SEAGROVE",
    "STALEY",

    // Wake County
    "APEX",
    "FUQUAY",
    "FUQUAY VARINA",
    "FUQUAY-VARINA",
    "NEW HILL"
  };
}
