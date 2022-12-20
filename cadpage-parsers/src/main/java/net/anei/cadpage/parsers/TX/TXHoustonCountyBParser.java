package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class TXHoustonCountyBParser extends DispatchSPKParser {

  public TXHoustonCountyBParser() {
    super("HOUSTON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "Ksp.NGCAD@ky.gov";
  }
}
