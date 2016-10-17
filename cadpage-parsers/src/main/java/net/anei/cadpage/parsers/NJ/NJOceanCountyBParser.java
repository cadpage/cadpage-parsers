package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NJOceanCountyBParser extends DispatchA19Parser {
  
  public NJOceanCountyBParser() {
    super("OCEAN COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "@trpolice.org,@alert.active911.com";
  }
 
}
