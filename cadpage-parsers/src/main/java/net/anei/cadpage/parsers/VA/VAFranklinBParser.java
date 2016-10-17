package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Franklin, VA
 */
public class VAFranklinBParser extends DispatchSouthernParser {

  public VAFranklinBParser() {
    super(CITY_LIST, "FRANKLIN", "VA", DSFLAG_ID_OPTIONAL | DSFLAG_FOLLOW_CROSS);
  }

  @Override
  public String getFilter() {
    return "smith@shso.org,dispatch2@shso.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CITY OF FRANKLIN ")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  protected void parseExtra(String sExtra, Data data) {
    int pt = sExtra.indexOf('-');
    if (pt >= 0) {
      data.strCall = sExtra.substring(0,pt).trim();
      data.strSupp = sExtra.substring(pt+1).trim();
    }
    else {
      super.parseExtra(sExtra, data);
    }
  }

  private static final String[] CITY_LIST = new String[] {
     "FRANKLIN",
     "COURTLAND",
     "SEDLEY"
   };
}
