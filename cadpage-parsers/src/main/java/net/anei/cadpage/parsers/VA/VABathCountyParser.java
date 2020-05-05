package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class VABathCountyParser extends DispatchSPKParser {
  
  public VABathCountyParser() {
    super("BATH COUNTY", "VA");
  }
  
  @Override
  public String getFilter() {
    return "NoReplyBathco911@bathcountyva.org";
  }
}
