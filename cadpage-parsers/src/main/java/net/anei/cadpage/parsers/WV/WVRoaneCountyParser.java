package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class WVRoaneCountyParser extends DispatchA46Parser {

  public WVRoaneCountyParser() {
    super("ROANE COUNTY", "WV");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("RC911 -")) return false;
    if (!super.parseMsg(subject, body, data)) return false;
    data.strSource = "";
    return true;
  }

}
