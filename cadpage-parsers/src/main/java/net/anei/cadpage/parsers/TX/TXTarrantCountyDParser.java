package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXTarrantCountyDParser extends DispatchOSSIParser {
  
  public TXTarrantCountyDParser() {
    super("TARRANT COUNTY", "TX", 
          "FYI? CALL ADDR! X X END");
  }
  
  @Override
  public String getFilter() {
    return "CAD@hursttx.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("donotreply:")) body = "CAD:" + body.substring(11);
    return super.parseMsg(body, data);
  }
}
