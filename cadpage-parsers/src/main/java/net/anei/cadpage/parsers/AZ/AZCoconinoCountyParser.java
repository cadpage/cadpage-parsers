package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class AZCoconinoCountyParser extends DispatchA27Parser {
  
  public AZCoconinoCountyParser() {
    super("COCONINO COUNTY", "AZ");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

}
