package net.anei.cadpage.parsers.AK;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class AKSkagwayParser extends DispatchA19Parser {

  public AKSkagwayParser() {
    super("SKAGWAY", "AK");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }

}
