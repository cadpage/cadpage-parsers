package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class WVLoganCountyParser extends DispatchA71Parser {

  public WVLoganCountyParser() {
    super("LOGAN COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }
}
