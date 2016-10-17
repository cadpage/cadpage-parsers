package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class TXGreggCountyParser extends DispatchA41Parser {

  public TXGreggCountyParser() {
    super(CITY_CODES, "GREGG COUNTY", "TX", "[A-Z]{2,3}");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CL", "CLARKSVILLE",
      "EA", "EASTON",
      "EM", "EAST MOUNTAIN",
      "GC", "GREGG COUNTY",
      "GL", "GLADEWATER",
      "HC", "HARRISON COUNTY",
      "KI", "KILGORE",
      "LA", "LAKEPORT",
      "LV", "LONGVIEW",
      "UC", "UPSHUR COUNTY",
      "UG", "UNION GROVE",
      "WC", "WARREN CITY",
      "WO", "WHITE OAK"
  });
}
