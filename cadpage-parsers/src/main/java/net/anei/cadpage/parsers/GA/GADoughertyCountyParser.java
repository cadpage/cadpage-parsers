package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class GADoughertyCountyParser extends DispatchOSSIParser {
  
  public GADoughertyCountyParser() {
    super("DOUGHERTY COUNTY", "GA",
         "CALL ADDR X! X? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "cad@dougherty.ga.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    int pt = body.indexOf("\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }
}
