package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NYCortlandCountyCParser extends DispatchA19Parser {
  
  public NYCortlandCountyCParser() {
    super("CORTLAND COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "PrebleRipNRun@fdcms.info,dispatch@cortland-co.org";
  }
 
}
