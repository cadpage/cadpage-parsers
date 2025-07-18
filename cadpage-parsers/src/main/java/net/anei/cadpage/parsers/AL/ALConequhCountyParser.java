package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class ALConequhCountyParser extends DispatchA46Parser {

  public ALConequhCountyParser() {
    super("CONEQUH COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "avoyelles911@pagingpts.com";
  }

}
