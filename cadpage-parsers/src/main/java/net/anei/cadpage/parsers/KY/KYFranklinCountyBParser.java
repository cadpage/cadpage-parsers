package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYFranklinCountyBParser extends DispatchA74Parser {

  public KYFranklinCountyBParser() {
    super("FRANKLIN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }
}
