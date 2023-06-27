package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOAndrewCountyAParser extends DispatchA33Parser {

  public MOAndrewCountyAParser() {
    super("ANDREW COUNTY", "MO", A33_X_ADDR_EXT);
    setupSpecialStreets("CEDAR", "COURT", "MARKET", "PARK", "PRICE");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@ANDREWCOUNTY.COM,NOREPLY@OMNIGO.COM";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = data.strCity.replace(".", "");
    return true;
  }

}
