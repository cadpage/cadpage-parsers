package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class NCMadisonCountyAParser extends DispatchSouthernParser {
  
  
  public NCMadisonCountyAParser() {
    super(CITY_LIST, "MADISON COUNTY", "NC", DSFLAG_OPT_DISPATCH_ID | DSFLAG_FOLLOW_CROSS | DSFLAG_ID_OPTIONAL);
  }
  
  @Override
  public String getFilter() {
    return "e911@madisoncountync.org,@madisoncountync.gov";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "HOT SPRINGS",
    "MARS HILL",
    "MARSHALL",

    "MARSHALL TWP",
    "LAUREL TWP",
    "MARS HILL TWP",
    "BEECH GLENN TWP",
    "WALNUT TWP",
    "HOT SPRINGS TWP",
    "EBBS CHAPEL TWP",
    "SPRING CREEK TWP",
    "SANDY MUSH TWP",
    "GRAPEVINE TWP",
    "REVERE RICE COVE TWP"
  };

}
