package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHAuglaizeCountyCParser extends DispatchEmergitechParser {
  
  public OHAuglaizeCountyCParser() {
    super(CITY_LIST, "AUGLAIZE COUNTY", "OH", TrailAddrType.INFO);
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "ST MARYS",
      "ST. MARYS",
      "SAINT MARYS",
      "WAPAKONETA",

      // Villages
      "BUCKLAND",
      "CRIDERSVILLE",
      "MINSTER",
      "NEW BREMEN",
      "NEW KNOXVILLE",
      "WAYNESFIELD",

      // Townships
      "CLAY",
      "DUCHOUQUET",
      "GERMAN",
      "GOSHEN",
      "JACKSON",
      "LOGAN",
      "MOULTON",
      "NOBLE",
      "PUSHETA",
      "SAINT MARYS",
      "SALEM",
      "UNION",
      "WASHINGTON",
      "WAYNE",

      // Census-designated places
      "NEW HAMPSHIRE",
      "SAINT JOHNS",
      "UNIOPOLIS",

      // Unincorporated communities
      "BULKHEAD",
      "EGYPT",
      "FRYBURG",
      "GEYER",
      "GLYNWOOD",
      "GUTMAN",
      "HOLDEN",
      "KOSSUTH",
      "LOCK TWO",
      "MOULTON",
      "SANTA FE",
      "SLATER",
      "VILLA NOVA",

      // Ghost towns
      "BAY",
      "PUSHETA TOWN",
      "RINEHARTS"
  };

}
