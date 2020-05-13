package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IAMarionCountyParser extends DispatchA47Parser {
  
  public IAMarionCountyParser() {
    super("MCSO Dispatch", CITY_LIST, "MARION COUNTY", "IA", ".*");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.marion.ia.us";
  }

  private static final String[] CITY_LIST = new String[]{
    // Cities
    "BUSSEY",
    "HAMILTON",
    "HARVEY",
    "KNOXVILLE",
    "MARYSVILLE",
    "MELCHER-DALLAS",
    "PELLA",
    "PLEASANTVILLE",
    "SWAN",

    // Unincorporated communities
    "ATTICA",
    "CLOUD",
    "COLUMBIA",
    "FLAGLER",
    "OTLEY",
    "PERSHING",
    "TRACY",

    // Townships
    "CLAY",
    "DALLAS",
    "FRANKLIN",
    "INDIANA",
    "KNOXVILLE",
    "LAKE PRAIRIE",
    "LIBERTY",
    "PLEASANT GROVE",
    "RED ROCK",
    "SUMMIT",
    "UNION",
    "WASHINGTON"
  };
}
