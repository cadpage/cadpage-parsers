package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALCoosaCountyAParser extends DispatchA71Parser {

  public ALCoosaCountyAParser() {
    super("COOSA COUNTY", "AL");
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("COTTAGE GROVE/HISSOP")) city = "";
    return city;
  }
}
