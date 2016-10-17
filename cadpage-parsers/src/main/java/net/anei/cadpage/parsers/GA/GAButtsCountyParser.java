package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Butts County, GA
 */
public class GAButtsCountyParser extends DispatchA19Parser {
  
  public GAButtsCountyParser() {
    super("BUTTS COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "active911@buttscounty.org";
  }
}
