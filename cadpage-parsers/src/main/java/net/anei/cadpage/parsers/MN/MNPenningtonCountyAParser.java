package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

/**
 * Pennington County, MN
 */

public class MNPenningtonCountyAParser extends DispatchA43Parser {
  
  public MNPenningtonCountyAParser() {
    super("PENNINGTON COUNTY", "MN");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strPriority.length() > 0) data.strPriority = data.strPriority.substring(0, 1);
    return true;
  }
  
}
