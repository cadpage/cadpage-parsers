package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALWinstonCountyCParser extends DispatchA71Parser {

  public ALWinstonCountyCParser() {
    super("WINSTON COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "winstondispatch@digitalrms.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("Unincorporated")) data.strCity = "";
    return true;
  }


}
