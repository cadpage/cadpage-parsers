package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;



public class LAIberiaParishParser extends DispatchSPKParser {

  public LAIberiaParishParser() {
    super("IBERIA PARISH", "LA");
  }

  @Override
  public String getFilter() {
    return "info@lamsgs.com,info@embmsgs.com,active911@iberia911.com";
  }
}
