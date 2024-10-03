package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALMontgomeryCountyBParser extends DispatchA71Parser {

  public ALMontgomeryCountyBParser() {
    super("MONTGOMERY COUNTY", "AL");
    setupProtectedNames("DUTCH AND NELL");
  }

  @Override
  public String getFilter() {
    return "cad@covington911.com";
  }

}
