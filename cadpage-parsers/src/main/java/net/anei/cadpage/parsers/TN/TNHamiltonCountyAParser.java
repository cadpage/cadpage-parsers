package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA15Parser;


public class TNHamiltonCountyAParser extends DispatchA15Parser {
  
  public TNHamiltonCountyAParser() {
    super("HAMILTON COUNTY", "TN");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("HAMILTON COUNTY")) data.strCity = "";
    else if (data.strCity.endsWith(" CATOOSA CO")) {
      data.strCross = append(data.strCity.substring(0,data.strCity.length()-10).trim(), " & ", data.strCross);
      data.strCity = "CATOOSA COUNTY";
      data.strState = "GA";
    }
    return true;
  }
}
