package net.anei.cadpage.parsers.WI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;



public class WIOzaukeeCountyParser extends DispatchA63Parser {  
  public WIOzaukeeCountyParser() {
    super(CITY_CODES, "OZAUKEE COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "Police@ci.mequon.wi.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Properties CITY_CODES = buildCodeTable (new String[]{
    "ME", "MEQUON"
  });
}
