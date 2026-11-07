package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA88Parser;

public class ALBlountCountyCParser extends DispatchA88Parser {

  public ALBlountCountyCParser() {
    super(ALBlountCountyParser.CITY_LIST, "BLOUNT COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "blount911-notification@blount911.secureemailportal.com";
  }
}
