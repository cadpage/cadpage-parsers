package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA76Parser;

public class ILMarionCountyParser extends DispatchA76Parser {

  public ILMarionCountyParser() {
    super("MARION COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "marioncad@marionpolicedept.com,CADAlerts@marioncounty911illinois.gov";
  }

}
