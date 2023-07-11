package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALTallapoosaCountyCParser extends DispatchSouthernParser {

  public ALTallapoosaCountyCParser() {
    super(CITY_LIST, "TALLAPOOSA COUNTY", "AL",
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_ID | DSFLG_TIME);
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_IMPLIED_INTERSECT;
  }

  private static final String[] CITY_LIST = new String[] {

      // Tallapoosa County
      // Cities
      "ALEXANDER CITY",
      "DADEVILLE",
      "TALLASSEE",

      // Towns
      "CAMP HILL",
      "DAVISTON",
      "GOLDVILLE",
      "JACKSONS GAP",
      "NEW SITE",

      // Census-designated places
      "HACKNEYVILLE",
      "OUR TOWN",
      "REELTOWN",

      // Unincorporated communities
      "ANDREW JACKSON",
      "BULGERS",
      "CHEROKEE BLUFFS",
      "CHURCH HILL",
      "DUDLEYVILLE",
      "FOSHEETON",
      "FROG EYE",

      // Coosa County
      // Towns
      "GOODWATER",
      "KELLYTON",
      "ROCKFORD",

      // Census-designated places
      "EQUALITY",
      "HANOVER",
      "HISSOP",
      "MOUNT OLIVE",
      "NIXBURG",
      "RAY",
      "STEWARTVILLE",
      "WEOGUFKA",

      // Unincorporated communities
      "DOLLAR",
      "FISHPOND",
      "HATCHET",
      "MARBLE VALLEY"
  };

}
