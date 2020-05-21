package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYHopkinsCountyParser extends DispatchA27Parser {
  
  public KYHopkinsCountyParser() {
    super("HOPKINS COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "mpd@madisonvillepd.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = body.replace("<br>\n", "\n");
    return super.parseHtmlMsg(subject, body, data);
  }
}
