package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;


public class ARPopeCountyBParser extends DispatchA57Parser {

  public ARPopeCountyBParser() {
    super("POPE COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "relay@popecountyar.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    // Discard ARPopeCountyA alerts
    if (body.contains("Service Call Type:")) return false;

    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
