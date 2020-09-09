package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MIAlconaCountyAParser extends DispatchSPKParser {

  public MIAlconaCountyAParser() {
    super("ALCONA COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "Alcona911@alcona-county.net";
  }
}
