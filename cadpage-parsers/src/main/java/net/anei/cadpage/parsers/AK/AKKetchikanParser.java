package net.anei.cadpage.parsers.AK;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class AKKetchikanParser extends DispatchA25Parser {

  public AKKetchikanParser() {
    super("KETCHIKAN", "AK");
  }

  @Override
  public String getFilter() {
    return "alerts@city.ketchikan.ak.us";
  }

}
