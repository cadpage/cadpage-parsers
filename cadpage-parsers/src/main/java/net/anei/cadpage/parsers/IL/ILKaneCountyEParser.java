package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class ILKaneCountyEParser extends DispatchH06Parser {

  public ILKaneCountyEParser() {
    super("KANE COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "@quadcom911.org";
  }
}
