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
     "KAY", "KAYSVILLE",
     "LAY", "LAYTON",
     "ODC", "ODC",
     "SUN", "SUNSET",
     "SW",  "SOUTH WEBER",
     "SYR", "SYRACUSE",
     "WCO", "WCO",
     "WP",  "WEST POINT"
  });

}
