package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class GANewtonCountyParser extends DispatchA19Parser {

  public GANewtonCountyParser() {
    super("NEWTON COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
}
