package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

/**
 * Morrow County, OH
 */
public class OHMorrowCountyAParser extends DispatchA1Parser {

  public OHMorrowCountyAParser() {
    super("MORROW COUNTY", "OH");
    setupGpsLookupTable(OHMorrowCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "mc911@rrohio.com,911text@rrohio.com,911text@mcems.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;

    OHMorrowCountyParser.fixCity(data);
    return true;
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return OHMorrowCountyParser.doAdjustMapAddress(sAddress);
  }
}
