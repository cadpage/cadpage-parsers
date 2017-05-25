package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class ILChampaignCountyParser extends DispatchA41Parser {
  
  public   ILChampaignCountyParser() {
    super(CITY_CODES, "CHAMPAIGN COUNTY", "IL", "[A-Z]{1,2}FD"); 
  }
  
  @Override
  public String getFilter() {
    return "CAD@METCAD911.ORG";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "URT", "URBANA"
  });
}
