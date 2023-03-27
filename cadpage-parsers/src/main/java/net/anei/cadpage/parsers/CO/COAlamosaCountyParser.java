package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class COAlamosaCountyParser extends DispatchH03Parser {

  public COAlamosaCountyParser() {
    super("ALAMOSA COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "CG@csp.noreply";
  }
}
