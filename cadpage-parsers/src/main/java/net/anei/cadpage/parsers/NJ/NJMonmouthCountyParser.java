package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Monmouth County, NJ

*/

public class NJMonmouthCountyParser extends GroupBestParser {

  public NJMonmouthCountyParser() {
    super(new NJMonmouthCountyAParser(), new NJMonmouthCountyBParser(),
          new NJMonmouthCountyCParser(), new NJMonmouthCountyDParser(),
          new NJMonmouthCountyFParser(), new NJMonmouthCountyGParser());
  }

  static final String[] CITY_LIST = new String[]{
      "ALLENHURST",
      "ALLENTOWN",
      "ALLENWOOD",
      "ASBURY PARK",
      "ATLANTIC HIGHLANDS",
      "AVON-BY-THE-SEA",
      "BELFORD",
      "BELMAR",
      "BRADLEY BEACH",
      "BRIELLE",
      "CLIFFWOOD BEACH",
      "DEAL",
      "EAST FREEHOLD",
      "EATONTOWN",
      "ENGLISHTOWN",
      "FAIR HAVEN",
      "FAIRVIEW",
      "FARMINGDALE",
      "FREEHOLD",
      "HIGHLANDS",
      "INTERLAKEN",
      "KEANSBURG",
      "KEYPORT",
      "LAKE COMO",
      "LEONARDO",
      "LINCROFT",
      "LITTLE SILVER",
      "LOCH ARBOUR",
      "LONG BRANCH",
      "MANASQUAN",
      "MATAWAN",
      "MONMOUTH BEACH",
      "MORGANVILLE",
      "NAVESINK",
      "NEPTUNE CITY",
      "NORTH MIDDLETOWN",
      "OAKHURST",
      "OCEAN GROVE",
      "OCEANPORT",
      "PORT MONMOUTH",
      "RAMTOWN",
      "RED BANK",
      "ROBBINSVILLE",
      "ROBERTSVILLE",
      "ROOSEVELT",
      "RUMSON",
      "SEA BRIGHT",
      "SEA GIRT",
      "SHARK RIVER HILLS",
      "SHREWSBURY",
      "SPRING LAKE",
      "SPRING LAKE HEIGHTS",
      "STRATHMORE",
      "TINTON FALLS",
      "UNION BEACH",
      "WANAMASSA",
      "WEST BELMAR",
      "WEST FREEHOLD",
      "WEST LONG BRANCH",
      "YORKETOWN"
  };
}
