package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHPrebleCountyParser extends DispatchEmergitechParser {
  
  public OHPrebleCountyParser() {
    super("PREBLESHERIFF:", CITY_LIST, "PREBLE COUNTY", "OH", TrailAddrType.PLACE);
  }
  
  @Override
  public String getFilter() {
    return "PREBLESHERIFF@swohio.twcbc.com";
  }

  private static final String[] CITY_LIST = new String[]{
    
    // City
    "EATON",

    // Villages
    "COLLEGE CORNER",
    "CAMDEN",
    "ELDORADO",
    "GRATIS",
    "LEWISBURG",
    "NEW PARIS",
    "VERONA",
    "WEST ALEXANDRIA",
    "WEST ELKTON",
    "WEST MANCHESTER",

    // Townships
    "DIXON",
    "GASPER",
    "GRATIS",
    "HARRISON",
    "ISRAEL",
    "JACKSON",
    "JEFFERSON",
    "LANIER",
    "MONROE",
    "SOMERS",
    "TWIN",
    "WASHINGTON",

    // Unincorporated communities

    "BRENNERSVILLE",
    "BRINLEY",
    "BROWNS",
    "CAMPBELLSTOWN",
    "CEDAR SPRINGS",
    "DADSVILLE",
    "EBENEZER",
    "ENTERPRISE",
    "FAIRHAVEN",
    "GETTYSBURG",
    "GREENBUSH",
    "HAMBURG",
    "INGOMAR",
    "MORNING SUN",
    "MUTTONVILLE",
    "NEW HOPE",
    "NEW LEXINGTON",
    "NEW WESTVILLE",
    "SUGAR VALLEY",
    "TALAWANDA SPRINGS",
    "WEST FLORENCE",
    "WEST SONORA",
    "WHEATVILLE",
    
    // Butler County
    "SOMERVILLE",
    
    // Montgomery County
    "GERMANTOWN"
  };
}
