package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class WIWinnebagoCountyCParser extends DispatchH03Parser {

  public WIWinnebagoCountyCParser() {
    super("WINNEBAGO COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return ".WI@co.winnebago.wi.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.endsWith(" Active911 Incident") && !subject.endsWith(" Active911 Notification")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }

}
