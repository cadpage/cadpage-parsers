package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class PAClarionCountyDParser extends DispatchSPKParser {
  
  public PAClarionCountyDParser() {
    super("CLARION COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "cad@oes.clarion.pa.us";
  }

}
