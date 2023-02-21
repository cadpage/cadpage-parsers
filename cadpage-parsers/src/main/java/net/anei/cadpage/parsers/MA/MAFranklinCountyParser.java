package net.anei.cadpage.parsers.MA;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class MAFranklinCountyParser extends DispatchA32Parser {

  public MAFranklinCountyParser() {
    super(CITY_LIST, "FRANKLIN COUNTY", "MA");
  }
  
  @Override
  public String getFilter() {
    return "GREENFIELDFDALERTS@GREENFIELD-MA.GOV";
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // City
    "GREENFIELD",
    
    // Towns
    "ASHFIELD",
    "BERNARDSTON",
    "BUCKLAND",
    "CHARLEMONT",
    "COLRAIN",
    "CONWAY",
    "DEERFIELD",
    "ERVING",
    "GILL",
    "HAWLEY",
    "HEATH",
    "LEVERETT",
    "LEYDEN",
    "MONROE",
    "MONTAGUE",
    "NEW SALEM",
    "NORTHFIELD",
    "ORANGE",
    "ROWE",
    "SHELBURNE",
    "SHUTESBURY",
    "SUNDERLAND",
    "WARWICK",
    "WENDELL",
    "WHATELY",

    // Census-designated places
    "DEERFIELD",
    "MILLERS FALLS",
    "NORTHFIELD",
    "ORANGE",
    "SHELBURNE FALLS",
    "SOUTH DEERFIELD",
    "TURNERS FALLS",
    
    // Other unincorporated communities
    "LAKE PLEASANT",
    "SATANS KINGDOM",
    "ZOAR"
  };
}
