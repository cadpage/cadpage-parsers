package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDIdahoCountyParser extends DispatchA19Parser {

  public IDIdahoCountyParser() {
    super("IDAHO COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "spillmannotify@idahocounty.org";
  }

}
