package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA17Parser;


public class PAVenangoCountyBParser extends DispatchA17Parser {
  
  public PAVenangoCountyBParser() {
    super("VENANGO COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "tyler.lyman@active911.com,911Center@co.venango.pa.us";
  }
}
