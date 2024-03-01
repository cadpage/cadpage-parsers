package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INBooneCountyBParser extends DispatchSPKParser {
  public INBooneCountyBParser() {
    super("BOONE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "interact@co.boone.in.us,CAD-Auto-Send@co.boone.in.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCode.length() > 0) {
      String temp = data.strCode;
      data.strCode = data.strCall;
      data.strCall = temp;
    }
    return true;
  }

}
