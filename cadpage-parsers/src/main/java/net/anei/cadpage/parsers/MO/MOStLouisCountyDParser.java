package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOStLouisCountyDParser extends DispatchA25Parser {
  
  public MOStLouisCountyDParser() {
    super("ST LOUIS COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "CADAlert@UCityMO.org";
  }
}
