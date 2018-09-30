package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class PAUnionCountyParser extends DispatchSPKParser {
  
  public PAUnionCountyParser() {
    super("UNION COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "cademail@unionco.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("CENTRE")) data.strCity = "CENTRE TWP";
    return true;
  }

}
