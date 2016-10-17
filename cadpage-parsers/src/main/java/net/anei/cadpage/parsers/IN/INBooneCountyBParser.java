package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INBooneCountyBParser extends DispatchSPKParser {
  public INBooneCountyBParser() {
    super("BOONE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "cadincidents@co.boone.in.us";
  }
}
