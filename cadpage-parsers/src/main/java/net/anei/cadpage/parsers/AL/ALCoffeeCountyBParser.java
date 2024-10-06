package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALCoffeeCountyBParser extends DispatchA71Parser {

  public ALCoffeeCountyBParser() {
    super("COFFEE COUNTY", "AL");
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strSupp.contains("_CLEAR")) data.msgType = MsgType.RUN_REPORT;
    return true;
  }

}
