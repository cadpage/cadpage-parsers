package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Allen County, OH
 */
public class OHAllenCountyAParser extends DispatchCiscoParser {

  public OHAllenCountyAParser() {
    super("ALLEN COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "interface@acso-oh.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strAddress = data.strAddress.replace("LAKERIDGE DR", "DEERCREEK CIR");
    data.strCross = data.strCross.replace("LAKERIDGE DR", "DEERCREEK CIR");
    return true;
  }
}
  