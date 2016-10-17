package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class CAButteCountyBParser extends DispatchProQAParser {
  
  public CAButteCountyBParser() {
    super("BUTTE COUNTY", "CA", "");
  }
  
  @Override
  public String getFilter() {
    return "paging@buttecountyems.org";
  }

}
