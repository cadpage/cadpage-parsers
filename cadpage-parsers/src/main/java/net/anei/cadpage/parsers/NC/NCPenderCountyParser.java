package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

/**
 * Pender County, NC
 */
public class NCPenderCountyParser extends DispatchA3Parser {
  
  private static final Pattern PFX_PTN = Pattern.compile("911-?:=?|=|");
  
  public NCPenderCountyParser() {
    super(0, PFX_PTN, "PENDER COUNTY", "NC");
    setupSaintNames("JOHNS");
  }

  @Override
  public String getFilter() {
    return "911-@pendersheriff.com";
  }
}
