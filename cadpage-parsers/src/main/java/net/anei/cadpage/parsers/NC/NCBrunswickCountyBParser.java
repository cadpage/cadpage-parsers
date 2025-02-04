package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCBrunswickCountyBParser extends DispatchA71Parser {

  public NCBrunswickCountyBParser() {
    super("BRUNSWICK COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;

    // To reject any NCBrunswickCountyC alerts
    if (data.strPriority.isEmpty()) return false;

    int pt = data.strPriority.indexOf(' ');
    if (pt >= 0) {
      data.strCode = data.strPriority.substring(pt+1).trim();
      data.strPriority = data.strPriority.substring(0,pt);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("PRI", "PRI CODE");
  }
}
