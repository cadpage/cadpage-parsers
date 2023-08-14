package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALCalhounCountyDParser extends DispatchA71Parser {

  public ALCalhounCountyDParser() {
    super("CALHOUN COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "psmith@bibbcosoal.org";
  }

}
