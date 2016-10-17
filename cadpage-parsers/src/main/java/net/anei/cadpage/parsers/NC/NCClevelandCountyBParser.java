package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCClevelandCountyBParser extends DispatchOSSIParser {
  
  public NCClevelandCountyBParser() {
    super("CLEVELAND COUNTY", "NC", "CALL ADDR! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@cityofshelby.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    return super.parseMsg(body, data);
  }

}
