package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INJayCountyParser extends DispatchA19Parser {

  public INJayCountyParser() {
    super("JAY COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "dispatch@co.jay.in.us";
  }
}
