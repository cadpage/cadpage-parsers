package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchC02Parser;

public class NYRocklandCountyIParser extends DispatchC02Parser {

  public NYRocklandCountyIParser() {
    super("ROCKLAND COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "monseyactive911@gmail.com";
  }
}
