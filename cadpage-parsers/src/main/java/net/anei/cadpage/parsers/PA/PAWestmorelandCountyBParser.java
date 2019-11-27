package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA44Parser;

public class PAWestmorelandCountyBParser extends DispatchA44Parser {
  
  public PAWestmorelandCountyBParser() {
    super("WESTMORELAND COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "nhtpdadmin@nhtpd.us";
  }

}
