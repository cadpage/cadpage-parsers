package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class KSChaseCountyParser extends DispatchBCParser {

  public KSChaseCountyParser() {
    super("CHASE COUNTY", "KS", DispatchA33Parser.A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

}
