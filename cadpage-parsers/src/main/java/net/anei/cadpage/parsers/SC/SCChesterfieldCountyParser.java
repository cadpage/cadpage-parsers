
package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class SCChesterfieldCountyParser extends DispatchSouthernParser {

  public SCChesterfieldCountyParser() {
    super(CITY_LIST, "CHESTERFIELD COUNTY", "SC", DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_X | DSFLG_NAME | DSFLG_PHONE |
        DSFLG_UNIT1 | DSFLG_OPT_ID | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
  }

  @Override
  public String getFilter() {
    return "CCCALLTEXT@SHTC.NET";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Fix special call type changed msg
    if (body.contains("CallType Changed to ")) {
      int pt = body.indexOf(',');
      body = body.substring(0,pt) + ",,," + body.substring(pt);
    }
    
    // Fixed place name that includes a comma
    body = body.replace(", INC,", ",");

    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{

      //TOWNS
      "CHERAW",
      "CHESTERFIELD",
      "JEFFERSON",
      "MC BEE",
      "MCBEE",
      "MOUNT CROGHAN",
      "MT CROGHAN",
      "PAGELAND",
      "PATRICK",
      "RUBY",

      //UNINCORPORATED COMMUNITIES

      "ANGELUS",
      "CASH",
      "MIDDENDORF",
      "MINDEN",
      
      // Darlington County
      "HARTSVILLE"
  };
}
