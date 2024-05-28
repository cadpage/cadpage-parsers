package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class VABedfordCountyCParser extends DispatchH03Parser {

  public VABedfordCountyCParser() {
    super("BEDFORD COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "EC@bedfordcountyva.gov";
  }

}
