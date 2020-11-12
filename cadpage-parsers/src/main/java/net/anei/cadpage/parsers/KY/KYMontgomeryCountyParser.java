package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYMontgomeryCountyParser extends DispatchA27Parser {

  public KYMontgomeryCountyParser() {
    super("MONTGOMERY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

}
