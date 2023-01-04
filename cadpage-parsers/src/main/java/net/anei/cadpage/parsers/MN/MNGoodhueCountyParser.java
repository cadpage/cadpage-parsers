package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Goodhue County, MN
 */

public class MNGoodhueCountyParser extends DispatchA27Parser {

  public MNGoodhueCountyParser() {
    super("GOODHUE COUNTY", "MN", "[A-Z]+\\d+[A-Z]?|Z[A-Z]+|[A-Z]{1,3}FD");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,CISActive911@co.goodhue.mn.us,donotreply@ci.red-wing.mn.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_CR;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\nNOTICE:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

}
