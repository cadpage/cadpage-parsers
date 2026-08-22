package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC09Parser;

public class FLPalmBeachCountyBParser extends DispatchC09Parser {

  public FLPalmBeachCountyBParser() {
    super("PALM BEACH COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "locutionalert@pbgfl.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\nCITY OF PALM BEACH");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

}
