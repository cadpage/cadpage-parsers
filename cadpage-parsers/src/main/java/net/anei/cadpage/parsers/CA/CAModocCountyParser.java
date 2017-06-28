package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;


public class CAModocCountyParser extends DispatchSPKParser {
  
  public CAModocCountyParser() {
    super("MODOC COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "sheriffoffice@modocsheriff.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " HILL UNITS");
    data.strCity = stripFieldEnd(data.strCity, " RURAL");
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.startsWith("CAL PINES")) return "ALTURAS";
    return city;
  }

}
