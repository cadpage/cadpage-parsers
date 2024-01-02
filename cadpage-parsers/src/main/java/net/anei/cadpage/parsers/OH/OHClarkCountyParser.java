package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/*
 * Clark County, OH
 */

public class OHClarkCountyParser extends GroupBestParser {

  public OHClarkCountyParser() {
    super(new OHClarkCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{

    // Cities
    "NEW CARLISLE",
    "SPRINGFIELD",

    // Villages
    "CATAWBA",
    "CLIFTON",
    "DONNELSVILLE",
    "ENON",
    "NORTH HAMPTON",
    "SOUTH CHARLESTON",
    "SOUTH VIENNA",
    "TREMONT CITY",

    // Townships
    "BETHEL TWP",
    "GERMAN TWP",
    "GREEN TWP",
    "HARMONY TWP",
    "MAD RIVER TWP",
    "MADISON TWP",
    "MOOREFIELD TWP",
    "PIKE TWP",
    "PLEASANT TWP",
    "SPRINGFIELD TWP",

    // Census-designated places
    "CRYSTAL LAKES",
    "GREEN MEADOWS",
    "HOLIDAY VALLEY",
    "NORTHRIDGE",
    "PARK LAYNE"
  };

}
