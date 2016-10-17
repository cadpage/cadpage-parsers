package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class NCMitchellCountyParser extends DispatchA3Parser {
  
  public NCMitchellCountyParser() {
    super(1, "Mitchell911:", "MITCHELL COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@main.nc.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data, boolean splitField) {
    if (!super.parseMsg(body, data, splitField)) return false;
    if (data.strPhone.equals("828- -")) data.strPhone = "";
    return true;
  }
  
}
