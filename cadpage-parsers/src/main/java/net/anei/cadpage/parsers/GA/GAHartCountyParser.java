package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAHartCountyParser extends DispatchSPKParser {

  public GAHartCountyParser() {
    super("HART COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "hartcong911@hartcountyga.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (!data.strCode.isEmpty()) {
      String tmp = data.strCode;
      data.strCode = data.strCall;
      data.strCall = tmp;
    }
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("BIO")) city = "HARTWELL";
    return city;
  }
}
