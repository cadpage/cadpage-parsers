package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;
/**
 * Village, TX
 */
public class TXHarrisCountyBParser extends DispatchProphoenixParser {

  public TXHarrisCountyBParser() {
    super(null, TXHarrisCountyParser.CITY_LIST, "HARRIS COUNTY", "TX");
  }

  public String getFilter() {
    return "contact@villagefire.org,DoNotReply@villagefire.org";
  }

}
