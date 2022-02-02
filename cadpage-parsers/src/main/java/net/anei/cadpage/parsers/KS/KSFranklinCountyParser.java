package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class KSFranklinCountyParser extends DispatchA72Parser {

  public KSFranklinCountyParser() {
    super("FRANKLIN COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "TPS_Service@tylertech.com,Tyler.Technologies@FranklinCoKs.org";
  }
}
