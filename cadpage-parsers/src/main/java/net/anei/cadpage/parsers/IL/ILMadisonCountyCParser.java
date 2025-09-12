package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class ILMadisonCountyCParser extends DispatchH06Parser {
  
  public ILMadisonCountyCParser() {
    super("MADISON COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "ripnrun@madisoncountyil.gov";
  }

}
