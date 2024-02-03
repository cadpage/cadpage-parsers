package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA76Parser;

public class VAKingWilliamCountyParser extends DispatchA76Parser {

  public VAKingWilliamCountyParser() {
    super("KING WILLIAM COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "CADAdmin@kingwilliamcounty.us,Scanner-IDNETWORKS@kwc.gov";
  }
}
