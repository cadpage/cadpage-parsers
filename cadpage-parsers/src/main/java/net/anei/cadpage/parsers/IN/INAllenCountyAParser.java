package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INAllenCountyAParser extends DispatchA19Parser {

  public INAllenCountyAParser() {
    super("ALLEN COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "911@allencounty.us,@nobleco.gov";
  }
}
