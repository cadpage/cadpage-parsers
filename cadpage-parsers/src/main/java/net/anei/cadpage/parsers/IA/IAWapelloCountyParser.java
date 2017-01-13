package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Wapello County, IA
 */

public class IAWapelloCountyParser extends DispatchA27Parser {
  
  public IAWapelloCountyParser() {
    super("WAPELLO COUNTY", "IA", "\\w+");
  }
  
  @Override
  public String getFilter() {
    return "alerts@ci.ottumwa.ia.us";
  }
}
