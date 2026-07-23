package net.anei.cadpage.parsers.WY;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class WYHotSpringsCountyParser extends DispatchA55Parser {

  public WYHotSpringsCountyParser() {
    super("HOT SPRINGS COUNTY", "WY");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
