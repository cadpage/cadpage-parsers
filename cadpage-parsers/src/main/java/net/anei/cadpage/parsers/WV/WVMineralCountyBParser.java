package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class WVMineralCountyBParser extends DispatchSPKParser {
  
  public WVMineralCountyBParser() {
    super("MINERAL COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "xdc@mineralcounty911.com,dispatchfire@comcast.net,mineraldispatch@gmail.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    // Subject is sometimes removed, in which case we will cludge it 
    if (subject.length() == 0) subject = " gets ";
    
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    
    if (data.strPlace.equals("WV - -")) data.strPlace = "";
    return true;
  }
  
}
