package net.anei.cadpage.parsers.WI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;



public class WIOzaukeeCountyParser extends DispatchA63Parser {  
  public WIOzaukeeCountyParser() {
    super(CITY_CODES, CITY_LIST, "OZAUKEE COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "Police@ci.mequon.wi.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final String[] CITY_LIST = new String[]{
    // Cities
    "Cedarburg",
    "MEQUON",
    "PORT WASHINGTON",
    
    // Villages
    "BAYSIDE",
    "BELGIUM",
    "FREDONIA",
    "GRAFTON",
    "NEWBURG",
    "SAUKVILLE",
    "THIENSVILLE",
    
    // Towns
    "BELGIUM",
    "CEDARBURG",
    "FREDONIA",
    "GRAFTON",
    "PORT WASHINGTON",
    "SAUKVILLE",
    
    // Census-designated place
    "WAUBEKA",
    
    // Unincorporated communities
    "DACADA",
    "DECKER",
    "DECKER CORNER",
    "DRUECKER",
    "HAMILTON",
    "HOLY CROSS",
    "HORNS CORNERS",
    "KNELLSVILLE",
    "LAKE CHURCH",
    "LAKEFIELD",
    "LITTLE KOHLER",
    "SAUK TRAIL BEACH",
    "ULAO",
    
    // Ghost town
    "STONEHAVEN"
  };
  
  private static final Properties CITY_CODES = buildCodeTable (new String[]{
    "ME", "MEQUON"
  });
}
