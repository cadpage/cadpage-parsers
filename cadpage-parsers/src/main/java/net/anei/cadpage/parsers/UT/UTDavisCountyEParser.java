package net.anei.cadpage.parsers.UT;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

public class UTDavisCountyEParser extends DispatchA11Parser {
  
  public UTDavisCountyEParser() {
    super(CITY_CODES, "DAVIS COUNTY", "UT");
  }
  
  @Override
  public String getFilter() {
    return "paging@daviscountyutah.gov";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANT", "ANTELOPE ISLAND",
      "CLF", "CLEARFIELD", 
      "CLN", "CLINTON",
      "CNE", "CENTERVILLE",  // or KAYESVILLE?
      "FAR", "FARMINGTON",
      "FHT", "FRUIT HEIGHTS",
      "HIL", "HAFB",
      "HOO", "HOOPER",
      "KAY", "KAYSVILLE",
      "LAY", "LAYTON",
      "ODC", "OGDEN",
      "OGD",  "OGDEN",
      "ROY", "ROY",
      "SUN", "SUNSET",
      "SW",  "SOUTH WEBER",
      "SYR", "SYRACUSE",
      "WCO", "WCO",
      "WP",  "WEST POINT"
  });

}
