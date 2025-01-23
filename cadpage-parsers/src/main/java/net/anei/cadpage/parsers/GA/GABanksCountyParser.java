package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class GABanksCountyParser extends DispatchSouthernParser {

  public GABanksCountyParser() {
    super(CITY_LIST, "BANKS COUNTY", "GA",
          DSFLG_ADDR | DSFLG_ID | DSFLG_TIME);
  }

  @Override
  public String getFilter() {
    return "911@co.banks.ga.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n----");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (super.parseMsg(body, data)) return true;
    if (pt < 0) return false;
    data.msgType = MsgType.GEN_ALERT;
    data.strSupp = body;
    return true;
  }


  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BALDWIN",
      "COMMERCE",
      "GILLSVILLE",
      "LULA",

      // Towns
      "ALTO",
      "HOMER",
      "MAYSVILLE",

      // Unincorporated communities
      "HOLLINGSWORTH",
      "NARROWS"
  };
}
