package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC15Parser;

public class MSPrentissCountyParser extends DispatchC15Parser {

  public MSPrentissCountyParser() {
    super("PRENTISS COUNTY", "MS");
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCity.equals("RIP")) data.strCity = "RIPLEY";
    return true;
  }
}
