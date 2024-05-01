package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA79Parser;

public class MIMacombCountyBParser extends DispatchA79Parser {

  public MIMacombCountyBParser() {
    super("Dispatch Information", "MACOMB COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "ESODispAlert@UniversalMacomb.com";
  }

}
