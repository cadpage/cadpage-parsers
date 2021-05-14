package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class ARDallasCountyParser extends DispatchBCParser {

  public ARDallasCountyParser() {
    super("DALLAS COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "DALLASCOUNTYNOTIFICATION@OMNIGO.COM,noreply@omnigo.com";
  }

}
