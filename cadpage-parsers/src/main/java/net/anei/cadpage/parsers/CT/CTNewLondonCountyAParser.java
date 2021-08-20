package net.anei.cadpage.parsers.CT;


import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;
/**
 * New London County, CT
 */
public class CTNewLondonCountyAParser extends DispatchA32Parser {

  public CTNewLondonCountyAParser() {
    super(CITY_LIST, "NEW LONDON COUNTY", "CT");
  }

  @Override
  public String getFilter() {
    return "ledyard911@ct.org,LEDYARD-911@LEDYARDCT.ORG,ledyard911@gmail.com";
  }

  private static final String[] CITY_LIST= new String[]{
    "EAST LYME",
    "GALES FERRY",
    "GROTON",
    "LEDYARD",
    "MONTVILLE",
    "NORWICH",
    "PRESTON"
  };
}
