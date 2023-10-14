package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ARBentonCountyIParser extends DispatchA71Parser {

  public ARBentonCountyIParser() {
    super("BENTON COUNTY", "AR");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Active 911")) return false;
    return super.parseMsg(subject, body, data);
  }

  @Override
  public int getMapFlags( ) {
    return MAP_FLG_PREFER_GPS;
  }

}
