package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class KSButlerCountyBParser extends DispatchA57Parser {
  
  public KSButlerCountyBParser() {
    super("BUTLER COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@bucoks.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    if (data.strCity.equals("County")) data.strCity = "";
    
    if (data.strAddress.startsWith("MM") && !data.strApt.isEmpty()) {
      data.strAddress = data.strAddress + ' ' + data.strApt;
      data.strApt = "";
    }
    return true;
  }

}
