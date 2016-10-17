package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MOCamdenCountyParser extends DispatchSPKParser {
  public MOCamdenCountyParser() {
    super("CAMDEN COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "dispatch@camdenmo.org";
  }
}
