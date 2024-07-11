package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class KYBowlingGreenParser extends DispatchH03Parser {

  public KYBowlingGreenParser() {
    super("BOWLING GREEN", "KY");
  }

  @Override
  public String getFilter() {
    return "PD@bgky.org,@premierone.local";
  }
}
