package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class GAGordonCountyParser extends DispatchA19Parser {

  public GAGordonCountyParser() {
    super("GORDON COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "gordone911@spillman.com,@ALERT.ACTIVE911.COM,gordone911@gordoncountyga.gov";
  }

}
