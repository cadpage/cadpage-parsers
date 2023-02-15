package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VAFranklinCountyCParser extends DispatchA71Parser {

  public VAFranklinCountyCParser() {
    super("FRANKLIN COUNTY", "VA");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strUnit = data.strUnit.replace(" ", "");
    return true;
  }
  
}
