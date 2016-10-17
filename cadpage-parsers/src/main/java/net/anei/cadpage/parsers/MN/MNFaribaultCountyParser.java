package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Faribault County, MN
 */
public class MNFaribaultCountyParser extends DispatchA27Parser {
  
  public MNFaribaultCountyParser() {
    super("FARIBAULT COUNTY", "MN", "[A-Z]{3}\\d{3}[A-Z]{2}|FR[A-Z]*\\d*|[A-Z]{3}|Faribault County");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
  