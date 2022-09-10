package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class WAMasonCountyParser extends DispatchA19Parser {

  public WAMasonCountyParser() {
    super("MASON COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,macecom@hcc.net";
  }
}
