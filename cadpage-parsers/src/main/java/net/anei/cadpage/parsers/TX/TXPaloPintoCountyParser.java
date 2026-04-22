package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXPaloPintoCountyParser extends DispatchA72Parser {

  public TXPaloPintoCountyParser() {
    super("PALO PINTO COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "admin207804@co.palo-pinto.tx.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strState.equals("CO")) {
      data.strCity = append(data.strCity, " ", data.strState);
      data.strState = "";
    }
    return true;
  }

}
