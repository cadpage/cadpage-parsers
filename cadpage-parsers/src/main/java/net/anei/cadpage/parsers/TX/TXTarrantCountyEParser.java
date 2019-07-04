package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXTarrantCountyEParser extends DispatchA18Parser {
  
  public TXTarrantCountyEParser() {
    super(TXTarrantCountyParser.CITY_LIST, "TARRANT COUNTY","TX");
  }
 
  @Override
  public String getFilter() {
    return "cad@evermantx.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0 || !body.startsWith("-")) return false;
    body = subject + '\n' + body;
    return super.parseMsg(body, data);
  }
}
