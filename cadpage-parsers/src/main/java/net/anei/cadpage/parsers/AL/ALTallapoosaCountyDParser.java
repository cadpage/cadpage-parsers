package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ALTallapoosaCountyDParser extends DispatchA19Parser {

  public ALTallapoosaCountyDParser() {
    super("TALLAPOOSA COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "rapidnotificationtallapoosa@gmail.com";
  }

}
