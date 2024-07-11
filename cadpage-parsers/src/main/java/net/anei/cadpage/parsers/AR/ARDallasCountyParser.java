package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ARDallasCountyParser extends DispatchA71Parser {

  public ARDallasCountyParser() {
    super("DALLAS COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "cadpage@e9.com,dallasco911@rpsfire.com";
  }

}
