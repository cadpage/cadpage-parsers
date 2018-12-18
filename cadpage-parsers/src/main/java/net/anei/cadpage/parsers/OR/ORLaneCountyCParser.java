package net.anei.cadpage.parsers.OR;


import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;



public class ORLaneCountyCParser extends DispatchA22Parser {
  
  public ORLaneCountyCParser() {
    super(CITY_CODES,"LANE COUNTY", "OR");
  }
  
  @Override
  public String getFilter() {
    return "NoReplyDispatch@co.lane.or.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CRE",   "CRESCENT",
      "DEX",   "DEXTER",
      "LANE",  "LANE COUNTY",
      "LOW",   "LOWELL",
      "OAK",   "OAKRIDGE",
      "WEF",   "WESTFIR"
  });
}
