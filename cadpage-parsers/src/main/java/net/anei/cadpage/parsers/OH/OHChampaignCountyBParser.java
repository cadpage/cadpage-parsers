package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA75Parser;


public class OHChampaignCountyBParser extends DispatchA75Parser {

  public OHChampaignCountyBParser() {
    super("CHAMPAIGN COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "lcso911@co.logan.oh.us,champaign911@co.champaign.oh.us";
  }
}
