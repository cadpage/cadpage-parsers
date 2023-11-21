package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;

/**
 * Botetourt County, VA
 */
public class VABotetourtCountyAParser extends DispatchSouthernPlusParser {

  public VABotetourtCountyAParser() {
    super(CITY_LIST, "BOTETOURT COUNTY", "VA",
          DSFLG_OPT_DISP_ID | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_BAD_PLACE | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME);
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
    data.strCity = convertCodes(data.strCity.toUpperCase(), FIX_CITY_TABLE);
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
    "GLASGOW",
    "GLASCOW",        // Misspelled
    "ROCKBRIDGE",
    "ROCKBRIDGE CO",
    "NATURAL BRIDGE STATION",

    // Independent cities
    "ROANOKE CITY"
  };

  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "GLASCOW",    "GLASGOW"
  });
}
