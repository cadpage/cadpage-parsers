package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;


public class NCMitchellCountyParser extends DispatchA71Parser {

  public NCMitchellCountyParser() {
    super("MITCHELL COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strPhone.equals("828- -")) data.strPhone = "";
    return true;
  }

}
