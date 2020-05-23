package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCMcDowellCountyBParser extends DispatchA71Parser {
  
  public NCMcDowellCountyBParser() {
    super("MCDOWELL COUNTY", "NC");
  }
  
  @Override
  public String adjustMapCity(String city) {
    return NCMcDowellCountyParser.doAdjustMapCity(city);
  }
}
