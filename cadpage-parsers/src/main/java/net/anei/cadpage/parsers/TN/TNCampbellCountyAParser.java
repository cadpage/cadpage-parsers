package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA89Parser;


public class TNCampbellCountyAParser extends DispatchA89Parser {

  public TNCampbellCountyAParser() {
    super("CAMPBELL COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@campbelltne911.net";
  }
}
