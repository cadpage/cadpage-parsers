package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHAshlandCountyAParser extends DispatchEmergitechParser {
  
  public OHAshlandCountyAParser() {
    super(true, CITY_LIST, "ASHLAND COUNTY", "OH");
  }

  private static final String[] CITY_LIST = new String[]{
    
      //City

      "ASHLAND",

      //Villages

      "BAILEY LAKES",
      "HAYESVILLE",
      "JEROMESVILLE",
      "LOUDONVILLE",
      "MIFFLIN",
      "PERRYSVILLE",
      "POLK",
      "SAVANNAH",

      //Townships

      "CLEAR CREEK",
      "GREEN",
      "HANOVER",
      "JACKSON",
      "LAKE",
      "MIFFLIN",
      "MILTON",
      "MOHICAN",
      "MONTGOMERY",
      "ORANGE",
      "PERRY",
      "RUGGLES",
      "SULLIVAN",
      "TROY",
      "VERMILLION",

      //Unincorporated communities

      "ALBION",
      "ENGLAND",
      "FIVE POINTS",
      "HEREFORK",
      "LAKE FORK",
      "MCKAY",
      "MCZENA",
      "MOHICANVILLE",
      "NANKIN",
      "NOVA",
      "PARADISE HILL",
      "REDHAW",
      "ROWSBURG",
      "RUGGLES",
      "SPRENG",
      "SULLIVAN",
      "WIDOWVILLE"


  };
}
