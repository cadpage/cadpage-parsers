package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSBrownCountyParser extends DispatchA25Parser {
  
  public KSBrownCountyParser() {
    super("BROWN COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "EnterpolAlerts@brownso.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("COUNTY")) data.strCity = "";
    return true;
  }
  
}
