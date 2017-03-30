package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class NJCumberlandCountyBParser extends DispatchProphoenixParser {
  
  public NJCumberlandCountyBParser() {
    super("CUMBERLAND COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "phoenix@vinelandcity.org";
  }

}
