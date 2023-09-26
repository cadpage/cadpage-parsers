package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA93Parser;

public class ILClintonCountyParser extends DispatchA93Parser {

  public   ILClintonCountyParser() {
    super("CLINTON COUNTY", "IL");
    setupProtectedNames("ROD AND GUN");
  }

  @Override
  public String getFilter() {
    return "dispatch@clintoncounty.us";
  }
}
