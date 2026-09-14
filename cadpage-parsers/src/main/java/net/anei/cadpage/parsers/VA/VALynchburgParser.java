package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC10Parser;

public class VALynchburgParser extends DispatchC10Parser {

  public VALynchburgParser() {
    super("LYNCHBURG", "VA");
  }

  @Override
  public String getFilter() {
    return "CAD@lynchburgva.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!body.startsWith("CAD:")) return false;
    body = body.substring(4).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

}
