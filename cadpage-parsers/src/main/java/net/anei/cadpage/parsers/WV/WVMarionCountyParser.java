package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA81Parser;

public class WVMarionCountyParser extends DispatchA81Parser {
  
  public WVMarionCountyParser() {
    super("MARION COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "marion911cad@gmail.com,marion911cad@marioncounty911.org,marion911@marioncountywv.com";
  }
}
