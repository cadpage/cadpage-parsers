package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class ILLakeCountyFParser extends DispatchH06Parser {

  public ILLakeCountyFParser() {
    super("LAKE COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "administrator@lakecounty911.org";
  }
}
