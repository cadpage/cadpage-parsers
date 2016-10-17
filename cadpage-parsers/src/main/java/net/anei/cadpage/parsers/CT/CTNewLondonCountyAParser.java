package net.anei.cadpage.parsers.CT;


import net.anei.cadpage.parsers.dispatch.DispatchA16Parser;
/**
 * New London County, CT
 */
public class CTNewLondonCountyAParser extends DispatchA16Parser {
  
  public CTNewLondonCountyAParser() {
    super(CITY_LIST, "NEW LONDON COUNTY", "CT");
  }

  @Override
  public String getFilter() {
    return "@montville-ct.org,dispatch@mail.eastlyme911.gov,ledyard911@ct.org";
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
