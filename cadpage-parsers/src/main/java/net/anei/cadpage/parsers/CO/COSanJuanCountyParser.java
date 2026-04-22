package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class COSanJuanCountyParser extends DispatchH03Parser {

  public COSanJuanCountyParser() {
    super("SAN JUAN COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "@CSP.CAD";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    int pt = data.strAddress.indexOf('(');
    if (pt >= 0) {
      data.strCall = append(data.strCall, " - ", stripFieldEnd(data.strAddress.substring(pt+1).trim(), ")"));
      data.strAddress = data.strAddress.substring(0,pt).trim();
    }
    return true;
  }
}
