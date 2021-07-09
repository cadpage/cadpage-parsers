package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;

public class WIWalworthCountyParser extends DispatchA63Parser {

  public WIWalworthCountyParser() {
    super("WALWORTH COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "lgpdrms@genevaonline.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return super.parseMsg(body, data);
  }
}
