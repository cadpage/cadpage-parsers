package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class TXFortBendCountyAParser extends DispatchA41Parser {

  public TXFortBendCountyAParser() {
    super(TXFortBendCountyParser.CITY_CODES, "FORT BEND COUNTY", "TX", "[A-Z]{2,3}\\d{0,2}");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CALLALERT:")) {
      body = "DISPATCH:" + body.substring(10);
    }
    return super.parseMsg(body, data);
  }
  
  
}
