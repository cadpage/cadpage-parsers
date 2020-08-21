package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Muhlenberg County, KY (A)
 */
public class KYMuhlenbergCountyAParser extends DispatchEmergitechParser {

  public KYMuhlenbergCountyAParser() {
    super(KYMuhlenbergCountyParser.CITY_LIST, "MUHLENBERG COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "pagegate@muhlenberg911.org";
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    // Occasionally the extra pace goes in 66 instead of 67.
    // But that only seems to happen to one word, so we will fix it here
    body = body.replace(" B ETWEEN ", " BETWEEN ");
    return super.parseMsg(body, data);
  }
}
