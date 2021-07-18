package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class WVMcDowellCountyParser extends DispatchSPKParser {
  
  public WVMcDowellCountyParser() {
    super("MCDOWELL COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "mccad@frontier.com,McDowell911-Relay@Lightrr.com";
  }

}
