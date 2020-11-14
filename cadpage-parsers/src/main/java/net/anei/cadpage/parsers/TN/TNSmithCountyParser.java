package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class TNSmithCountyParser extends DispatchSouthernParser {

  public TNSmithCountyParser() {
    super(CITY_LIST, "SMITH COUNTY", "TN",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_X | DSFLG_NAME | DSFLG_PHONE | DSFLG_ID | DSFLG_TIME);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("GORDONSVILLE-SMITH")) data.strCity = "GORDONSVILLE";
    if (!data.strApt.isEmpty() && data.strAddress.startsWith("I-40")) {
      data.strAddress = data.strAddress + ' ' + data.strApt;
      data.strApt = "";
    }
    return true;
  }


  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CARTHAGE",
      "GORDONSVILLE",
      "GORDONSVILLE-SMITH",
      "SOUTH CARTHAGE",

      // Unincorporated communities
      "BRUSH CREEK",
      "CHESTNUT MOUND",
      "DEFEATED",
      "DIFFICULT",
      "DIXON SPRINGS",
      "ELMWOOD",
      "HICKMAN",
      "KEMPVILLE",
      "LANCASTER",
      "NEW MIDDLETON",
      "PLEASANT SHADE",
      "RIDDLETON",
      "ROME",

      // Putname County
      "COOKEVILLE"
  };
}
