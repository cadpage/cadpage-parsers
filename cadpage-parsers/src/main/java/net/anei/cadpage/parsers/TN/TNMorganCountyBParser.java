package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class TNMorganCountyBParser extends DispatchA65Parser {

  public TNMorganCountyBParser() {
    super(CITY_LIST, "MORGAN COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@911email.org,dispatch@911comm1.info,@MorganCountyTNE911.com";
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "HARRIMAN",
    "SUNBRIGHT",
    "WARTBURG",

    // Towns
    "OAKDALE",
    "OLIVER SPRINGS",

    // Census-designated places
    "COALFIELD",
    "PETROS",

    // Unincorporated communities
    "BURRVILLE",
    "CHESTNUT RIDGE",
    "DEER LODGE",
    "JOYNER",
    "LANCING",
    "MOSSY GROVE",
    "RUGBY"
  };
}
