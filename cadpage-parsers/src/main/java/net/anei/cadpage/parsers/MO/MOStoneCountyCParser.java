package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStoneCountyCParser extends MOBarryCountyCParser {
  public MOStoneCountyCParser() {
    super("STONE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "stoneco911@gmail.com,stonedispatch@sces911.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    return super.parseHtmlMsg(subject, body, data);
  }

}
