package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class SCAndersonCountyCParser extends DispatchSPKParser {
  
  public SCAndersonCountyCParser() {
    super("ANDERSON COUNTY", "SC");
  }
  
  @Override
  public String getFilter() {
    return "anderson911@andersonsheriff.com,anderson911@andersoncountysc.org";
  }
}
