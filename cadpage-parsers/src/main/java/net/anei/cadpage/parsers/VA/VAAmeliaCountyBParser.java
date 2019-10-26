package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA76Parser;

public class VAAmeliaCountyBParser extends DispatchA76Parser {

  public VAAmeliaCountyBParser() {
    super("AMELIA COUNTY", "VA");
  }
  
  @Override
  public String getFilter() {
    return "cad@ameliasheriff.org";
  }
}
