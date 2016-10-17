package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MOBarryCountyCParser extends DispatchSPKParser {
  public MOBarryCountyCParser() {
    super("BARRY COUNTY", "MO");
    setupGpsLookupTable(MOBarryCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "dispatch@barrycountye911.org";
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address, String apt) {
    return MOBarryCountyParser.fixGpsLookupAddress(address, apt);
  }

  @Override
  public boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("COUNTY")) data.strCity = "";
    return true;
  }
  
}
