package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class COGilpinCountyParser extends DispatchOSSIParser {
  
  public COGilpinCountyParser() {
    super("GILPIN COUNTY", "CO", 
          "FYI CALL ADDR! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.gilpin.co.u";
  }

}
