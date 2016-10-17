package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA44Parser;

public class OHFairfieldParser extends DispatchA44Parser {

  public OHFairfieldParser() {
    super("BUTLER COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "firedispatch@fairfield-city.org";
  }
  
}
