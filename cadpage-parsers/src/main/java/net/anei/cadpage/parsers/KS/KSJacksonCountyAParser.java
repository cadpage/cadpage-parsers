package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSJacksonCountyAParser extends DispatchA25Parser {


  public KSJacksonCountyAParser() {
    super("JACKSON COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "CADAlerts@jasoks.org";
  }
  
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("County")) data.strCity = "";
    return true;
  }
}
