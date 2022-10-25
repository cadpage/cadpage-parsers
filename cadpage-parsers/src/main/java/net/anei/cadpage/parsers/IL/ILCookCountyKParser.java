package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA76Parser;

public class ILCookCountyKParser extends DispatchA76Parser {

  public ILCookCountyKParser() {
    super("COOK COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "cad@harwoodheights.org,cad@mcde911.org";
  }
}
