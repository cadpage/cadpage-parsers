package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOWashingtonCityParser extends DispatchA33Parser {

  public MOWashingtonCityParser() {
    super("WASHINGTON", "MO", A33_X_ADDR_EXT);
  }

  @Override
  public String getLocName() {
    return "Washington City, MO";
  }

  @Override
  public String getFilter() {
    return "DISPATCH@OMNIGO.COM,DISPATCH@CI.WASHINGTON.MO";
  }

}
