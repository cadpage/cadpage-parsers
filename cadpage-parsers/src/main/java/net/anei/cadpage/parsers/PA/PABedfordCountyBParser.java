package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class PABedfordCountyBParser extends DispatchA57Parser {
  
  public PABedfordCountyBParser() {
    super("BEDFORD COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "CADnoreply@bedfordcountypa.org";
  }

}
