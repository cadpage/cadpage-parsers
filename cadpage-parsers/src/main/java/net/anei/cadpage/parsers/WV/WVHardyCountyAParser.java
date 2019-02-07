package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;



public class WVHardyCountyAParser extends DispatchB3Parser {
  
  public WVHardyCountyAParser() {
    super("HARDYCOE911:", CITY_LIST, "HARDY COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "HARDYCOE911@hardynet.com";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }

  static final String[] CITY_LIST = new String[]{  
    "ARKANSAS",
    "BAKER",
    "BASORE",
    "BASS",
    "BAUGHMAN SETTLEMENT",
    "BEAN SETTLEMENT",
    "BRAKE",
    "CUNNINGHAM",
    "DURGON",
    "FISHER",
    "FLATS",
    "FORT RUN",
    "INKERMAN",
    "KESSEL",
    "LOST CITY",
    "LOST RIVER",
    "MATHIAS",
    "MCCAULEY",
    "MCNEILL",
    "MILAM",
    "MOOREFIELD",
    "NEEDMORE",
    "OLD FIELDS",
    "PERRY",
    "PERU",
    "RIG",
    "ROCK OAK",
    "ROCKLAND",
    "TANNERY",
    "TAYLOR",
    "WALNUT BOTTOM",
    "WARDENSVILLE"
    
  }; 
}
