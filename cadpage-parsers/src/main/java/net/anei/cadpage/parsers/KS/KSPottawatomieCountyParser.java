package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSPottawatomieCountyParser extends DispatchA25Parser {
  
  public KSPottawatomieCountyParser() {
    super("POTTAWATOMIE COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "EnterpolAlerts@atchisonlec.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    int pt = data.strAddress.lastIndexOf(" - ");
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", data.strAddress.substring(0,pt).trim());
      data.strAddress = data.strAddress.substring(pt+3).trim();
    }
    return true;
  }

  
}
