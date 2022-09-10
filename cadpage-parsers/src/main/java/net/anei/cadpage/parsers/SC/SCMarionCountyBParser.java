package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class SCMarionCountyBParser extends DispatchSPKParser {
  
  public SCMarionCountyBParser() {
    super("MARION COUNTY", "SC");
  }
  
  @Override
  public String getFilter() {
    return "marionsc911@gmail.com,marionsc911@marionsc.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strPlace.equals("SC - -")) data.strPlace = "";
    return true;
  }

}
