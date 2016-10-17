package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA16Parser;

/**
 * Hocking County, OH
 */
public class OHHockingCountyParser extends DispatchA16Parser {

  public OHHockingCountyParser() {
    super(CITY_LIST, "HOCKING COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "hockingcounty911@gmail.com";
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("SOUTH BLOOMING")) city = "SOUTH BLOOMINGVILLE";
    return city;
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "LOGAN",
    "WEST LOGAN",

    // Villages
    "BUCHTEL",
    "LAURELVILLE",
    "MURRAY CITY",

    // Townships
    "BENTON TWP",
    "FALLS TWP",
    "GOOD HOPE TWP",
    "GREEN TWP",
    "LAUREL TWP",
    "MARION TWP",
    "PERRY TWP",
    "SALT CREEK TWP",
    "STARR TWP",
    "WARD TWP",
    "WASHINGTON TWP",

    // Census-designated places
    "CARBON HILL",
    "HAYDENVILLE",
    "HIDEAWAY HILLS",
    "ROCKBRIDGE",

    // Unincorporated communities
    "EWING",
    "ILESBORO",
    "SAND RUN",
    "SOUTH BLOOMING",
    "SOUTH BLOOMINGVILLE",
    "UNION FURNACE",
    "SOUTH PERRY",
    
    // Athens County
    "NELSONVILLE",
    
    "PERRY COUNTY"
  };
}
  