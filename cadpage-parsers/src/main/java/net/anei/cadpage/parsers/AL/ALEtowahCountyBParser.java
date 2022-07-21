package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA89Parser;

public class ALEtowahCountyBParser extends DispatchA89Parser {

  public ALEtowahCountyBParser() {
    super("ETOWAH COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info,dispatch@etowah911.info";
  }
}
