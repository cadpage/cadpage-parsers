package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

/**
 * Camden County, GA
 */

public class GACamdenCountyAParser extends DispatchA57Parser {
  
  public GACamdenCountyAParser() {
    super("CAMDEN COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "911paging@camdensheriff.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return super.parseMsg(body, data);
  };
}
