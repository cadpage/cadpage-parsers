package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class KSMontgomeryCountyBParser extends DispatchA72Parser {

  public KSMontgomeryCountyBParser() {
    super("MONTGOMERY COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "noreply@independenceks.gov";
  }
}
