package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA44Parser;

public class NJMarlboroParser extends DispatchA44Parser {

  public NJMarlboroParser() {
    super("MARLBORO", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "ripandrun@marlboropd.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf("\nCONFIDENTIALITY NOTICE:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    return super.parseMsg(subject, body, data);
  }
  
}
