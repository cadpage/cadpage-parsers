package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;



public class SCDorchesterCountyAParser extends DispatchA71Parser {

  public SCDorchesterCountyAParser() {
    super("DORCHESTER COUNTY", "SC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("359")) data.strCity = "SUMMERVILLE";
    return true;
  }

}
