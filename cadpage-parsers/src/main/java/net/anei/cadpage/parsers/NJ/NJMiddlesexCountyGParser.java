package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class NJMiddlesexCountyGParser extends DispatchSPKParser {
  
  public NJMiddlesexCountyGParser() {
    super("MIDDLESEX COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "ipo911po@rutgers.edu";
  }
}
