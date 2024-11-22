package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class TNMauryCountyParser extends DispatchH03Parser {

  public TNMauryCountyParser() {
    super("MAURY COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "911@P1CAD.CommandCentral.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCity.equals("CNTY")) data.strCity = "";
    return true;
  }

}
