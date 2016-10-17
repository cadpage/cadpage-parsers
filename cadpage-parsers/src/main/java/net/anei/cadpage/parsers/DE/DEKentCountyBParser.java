package net.anei.cadpage.parsers.DE;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchChiefPagingParser;

/**
 * Kent County, DE
 */
public class DEKentCountyBParser extends DispatchChiefPagingParser {
  
  public DEKentCountyBParser() {
    this("KENT COUNTY", "DE");
  }

  protected DEKentCountyBParser(String defCity, String defState) {
    super(DEKentCountyBaseParser.CITY_LIST, defCity, defState);
    setupMultiWordStreets(DEKentCountyBaseParser.MULTI_WORD_STREET_LIST);
    setupProtectedNames(DEKentCountyBaseParser.MULTI_WORD_STREET_LIST);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject,  body, data)) return false;
    DEKentCountyBaseParser.adjustCityState(data);
    return true;
  }
}
