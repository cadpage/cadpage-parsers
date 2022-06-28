package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class ALMadisonCountyCParser extends DispatchA24Parser {
  
  public ALMadisonCountyCParser() {
    super("MADISON COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strSupp = stripFieldStart(data.strSupp, "// ");
    data.strSupp = data.strSupp.replace(" // ", "\n");
    return true;
  }
  
}
