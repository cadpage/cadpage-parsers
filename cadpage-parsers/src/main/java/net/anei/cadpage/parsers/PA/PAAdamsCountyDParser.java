package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class PAAdamsCountyDParser extends DispatchA57Parser {

  public PAAdamsCountyDParser() {
    super("ADAMS COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "CAD@adams911.com";
  }
}
