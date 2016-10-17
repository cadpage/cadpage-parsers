package net.anei.cadpage.parsers.WA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

/**
 * Pierce County, WA
 */
public class WAPierceCountyDParser extends DispatchA41Parser {
  
  public WAPierceCountyDParser() {
    super(CITY_CODES, "PIERCE COUNTY", "WA", "F\\d\\d");
  }
  
  @Override
  public String getFilter() {
    return "2082524514,Fire.comm@westpierce.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    return WAPierceCountyParser.adjustMapAddressCommon(sAddress);
  }
  
  @Override
  public String adjustMapCity(String map) {
    
    // City codes are fire districts that do not map well to 
    // actual recognizable city names
    return "";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "03", "WEST PIERCE",
      "05", "GIG HARBOR",
      "06", "CENTRAL PIERCE",
      "13", "PIERCE COUNTY",
      "14", "RIVERSIDE",
      "16", "KEY PENINSULA",
      "17", "SOUTH PIERCE",
      "18", "ORTING VALLEY",
      "21", "GRAHAM",
      "22", "EAST PIERCE",
      "23", "ASHFORD ELBE",
      "25", "CRYSTAL MOUNTAIN",
      "26", "ENUMCLAW",
      "27", "ANDERSON ISLAND",
      "43", "EATONVILLE",
      "49", "DUPONT",
      "52", "BUCKLEY"
  });
}
