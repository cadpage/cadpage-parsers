package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GADoughertyCountyBParser extends DispatchSPKParser {

  public GADoughertyCountyBParser() {
    super("DOUGHERTY COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "lray.cook911@yahoo.com";
  }
}
