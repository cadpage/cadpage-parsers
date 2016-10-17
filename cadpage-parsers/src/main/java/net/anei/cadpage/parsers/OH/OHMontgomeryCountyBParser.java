package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Montgomery County, OH
 */
public class OHMontgomeryCountyBParser extends DispatchCiscoParser {
  
  public OHMontgomeryCountyBParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "ciscopaging@vandaliaohio.org";
  }
  
  private static final String[] CITY_LIST = new String[]{

    "BROOKVILLE",
    "BUTLER",
    "CARLISLE",
    "CENTERVILLE",
    "CLAY",
    "CLAYTON",
    "DAYTON",
    "ENGLEWOOD",
    "FARMERSVILLE",
    "GERMAN",
    "GERMANTOWN",
    "HARRISON",
    "HUBER HEIGHTS",
    "JACKSON",
    "JEFFERSON",
    "KETTERING",
    "MIAMI",
    "MIAMISBURG",
    "MORAINE",
    "NEW LEBANON",
    "OAKWOOD",
    "PERRY",
    "PHILLIPSBURG",
    "RIVERSIDE",
    "SPRINGBORO",
    "TROTWOOD",
    "UNION",
    "VANDALIA",
    "VERONA",
    "WASHINGTON",
    "WEST CARROLLTON"

  };
}
