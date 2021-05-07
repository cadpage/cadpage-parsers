package net.anei.cadpage.parsers.UT;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class UTKaneCountyParser extends DispatchA19Parser {

  public UTKaneCountyParser() {
    super("KANE COUNTY", "UT");
  }

  @Override
  public String getFilter() {
    return "911@kane.utah.gov";
  }

}
