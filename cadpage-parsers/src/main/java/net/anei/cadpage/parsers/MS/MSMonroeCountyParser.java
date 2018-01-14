package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

public class MSMonroeCountyParser extends DispatchGeoconxParser {
  
  public MSMonroeCountyParser() {
    super("MONROE COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm1.info";
  }
}
