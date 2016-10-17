package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

/**
 * Jefferson County, AL (B)
 */
public class ALJeffersonCountyBParser extends DispatchA9Parser {
  
  public ALJeffersonCountyBParser() {
    super("JEFFERSON COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "Brian.Bonner@homewoodal.org,nws@mtnbrook.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " PD");
    data.strCity = stripFieldStart(data.strCity, "Unincorporated ");
    return true;
  }
  
}


