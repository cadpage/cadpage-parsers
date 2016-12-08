package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOMariesCountyParser extends DispatchA25Parser {
  
  public MOMariesCountyParser() {
    super("MARIES COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "cademailalert@mariesco.org";
  }
}
