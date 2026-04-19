package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALJeffersonCountyJParser extends DispatchA71Parser {

  public ALJeffersonCountyJParser() {
    super("JEFFERSON COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "administrator@trussville.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Eliminate ALJeffersonCountyI alerts
    if (subject.equals("ACTIVE 9-1-1")) return false;
    if (subject.equals("D I S P A T C H")) return false;
    return super.parseMsg(subject, body, data);
  }

}
