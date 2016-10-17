package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class KYErlangerDispatchParser extends DispatchA1Parser {

  public KYErlangerDispatchParser() {
    super("KENTON COUNTY", "KY"); 
  }
  
  @Override
  public String getFilter() {
    return "pscc@ci.erlanger.ky.us,administrator@erlangerpd.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals(data.strBox)) data.strCity = "";
    return true;
  }
  
}
