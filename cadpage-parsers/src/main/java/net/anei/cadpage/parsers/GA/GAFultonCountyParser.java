package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAFultonCountyParser extends DispatchSPKParser {

  public GAFultonCountyParser() {
    super("FULTON COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "lray.cook911@yahoo.com";
  }

}
