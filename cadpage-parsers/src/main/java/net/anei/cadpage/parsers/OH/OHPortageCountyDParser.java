package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


public class OHPortageCountyDParser extends DispatchA9Parser {

  public OHPortageCountyDParser() {
    super("PORTAGE COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "BROCMURPHY@GMAIL.COM,dispatch@kent.edu,dispatch@police.kent.edu";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = stripFieldStart(data.strCity, "City of ");
    data.strMap = stripFieldStart(data.strMap, "City of ");
    return true;
  }

}
