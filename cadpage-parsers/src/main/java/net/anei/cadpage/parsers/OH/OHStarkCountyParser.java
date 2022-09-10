package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/*
 * Stark County, OH
 */

public class OHStarkCountyParser extends GroupBestParser {

  public OHStarkCountyParser() {
    super(new OHStarkCountyAllianceParser(),
          new OHStarkCountyRedcenterParser(),
          new OHStarkCountyRedcenter2Parser(),
          new OHStarkCountyDParser(),

          new GroupBlockParser(),
          new OHStarkCountyCencommParser()
          );
  }

  static final String[] CITY_LIST = new String[]{

    // Cities
    "ALLIANCE",
    "CANAL FULTON",
    "CANTON",
    "LOUISVILLE",
    "MASSILLON",
    "NORTH CANTON",

    // Villages
    "BEACH CITY",
    "BREWSTER",
    "EAST CANTON",
    "EAST SPARTA",
    "HARTVILLE",
    "HILLS AND DALES",
    "LIMAVILLE",
    "MAGNOLIA",
    "MINERVA",
    "MEYERS LAKE",
    "NAVARRE",
    "WAYNESBURG",
    "WILMOT",

    // Townships
    "BETHLEHEM TWP",
    "CANTON TWP",
    "JACKSON TWP",
    "LAKE TWP",
    "LAWRENCE TWP",
    "LEXINGTON TWP",
    "MARLBORO TWP",
    "NIMISHILLEN TWP",
    "OSNABURG TWP",
    "PARIS TWP",
    "PERRY TWP",
    "PIKE TWP",
    "PLAIN TWP",
    "SANDY TWP",
    "SUGAR CREEK TWP",
    "TUSCARAWAS TWP",
    "WASHINGTON TWP",

    // Census-designated places
    "GREENTOWN",
    "PERRY HEIGHTS",
    "UNIONTOWN",

    // Other localities
    "AVONDALE",
    "CAIRO",
    "MARCHAND",
    "MAXIMO",
    "MIDDLEBRANCH",
    "NEW FRANKLIN",
    "NORTH INDUSTRY",
    "NORTH LAWRENCE",
    "PARIS",
    "RICHVILLE",
    "ROBERTSVILLE",
    "WACO",
  };

}
