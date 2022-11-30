package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOOzarkCountyParser extends DispatchA33Parser {

  public MOOzarkCountyParser() {
    super("OZARK COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
