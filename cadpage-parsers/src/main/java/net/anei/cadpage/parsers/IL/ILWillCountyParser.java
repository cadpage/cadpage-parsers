package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class ILWillCountyParser extends DispatchH03Parser {

  public ILWillCountyParser() {
    super("WILL COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "LC@willcounty911.gov";
  }

}
