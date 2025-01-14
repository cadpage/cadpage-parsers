package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NYLewisCountyBParser extends DispatchA19Parser {

  public NYLewisCountyBParser() {
    super("LEWIS COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "Lewis911@cnymail.com,no-reply@cnymail.com,@alert.active911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = stripFieldStart(data.strCity, "V/");
    return true;
  }

}
