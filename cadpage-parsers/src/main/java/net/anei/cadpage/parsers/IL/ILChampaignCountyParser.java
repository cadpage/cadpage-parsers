package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;



public class ILChampaignCountyParser extends DispatchA49Parser {
  
  public ILChampaignCountyParser() {
    super(CITY_CODES, "CHAMPAIGN COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "CAD@METCAD911.ORG";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      
      "URT", "URBANA"
      
  });

}
