package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ILCrawfordCountyParser extends DispatchA19Parser {
  
  public ILCrawfordCountyParser() {
    super(CITY_CODES, "CRAWFORD COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "noreply@robinsonpolice.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANN", "ANNAPOLIS",
      "BOG", "BOGOTA",
      "CAS", "CASEY",
      "CLA", "CLAREMONT",
      "DIE", "DIETERICH",
      "FLA", "FLAT ROCK",
      "HID", "HIDALGO",
      "HUN", "HUNT CITY",
      "HUT", "HUTSONVILLE",
      "ING", "INGRAHAM",
      "MON", "MONTROSE",
      "NEW", "NEWTON",
      "NOB", "NOBLE",
      "OBL", "OBLONG",
      "PAL", "PALESTINE",
      "ROB", "ROBINSON",
      "ROS", "ROSE HILL",
      "STM", "STE MARIE",
      "STO", "STOY",
      "TEU", "TEUTOPOLIS",
      "WES", "WEST YORK",
      "WHE", "WHEELER",
      "WIL", "WILLOW HILL",
      "WLB", "WEST LIBERTY",
      "YAL", "YALE"

  });

}
