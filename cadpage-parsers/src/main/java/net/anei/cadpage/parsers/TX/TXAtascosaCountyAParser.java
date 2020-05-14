package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXAtascosaCountyAParser extends DispatchA19Parser {
  
  public TXAtascosaCountyAParser() {
    super(TXAtascosaCountyParser.CITY_CODES, "ATASCOSA COUNTY", "TX");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Rapid Notification")) return false;
    return super.parseMsg("", body, data);
  }

  @Override
  public String getFilter() {
    return "so-noreply@acso-tx.org";
  }
}
