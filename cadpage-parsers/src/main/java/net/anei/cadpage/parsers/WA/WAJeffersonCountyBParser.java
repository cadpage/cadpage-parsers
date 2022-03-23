package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class WAJeffersonCountyBParser extends DispatchA57Parser {

  public WAJeffersonCountyBParser() {
    super("JEFFERSON COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us";
  }

}
