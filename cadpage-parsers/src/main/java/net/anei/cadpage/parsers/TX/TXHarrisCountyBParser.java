package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;
/**
 * Village, TX
 */
public class TXHarrisCountyBParser extends DispatchProphoenixParser {
  
  public TXHarrisCountyBParser() {
    super(null, CITY_LIST, "HARRIS COUNTY", "TX");
  }
  
  public String getFilter() {
    return "contact@villagefire.org";
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Multi-county cities
    "BAYTOWN",
    "FRIENDSWOOD",
    "HOUSTON",
    "KATY",
    "LEAGUE CITY",
    "MISSOURI CITY",
    "PEARLAND",
    "SEABROOK",
    "STAFFORD",
    "WALLER",

    // Cities
    "BELLAIRE",
    "BUNKER HILL VILLAGE",
    "DEER PARK",
    "EL LAGO",
    "GALENA PARK",
    "HEDWIG VILLAGE",
    "HILSHIRE VILLAGE",
    "HUMBLE",
    "HUNTERS CREEK VILLAGE",
    "JACINTO CITY",
    "JERSEY VILLAGE",
    "LA PORTE",
    "MORGAN'S POINT",
    "NASSAU BAY",
    "PASADENA",
    "PINEY POINT VILLAGE",
    "SHOREACRES",
    "SOUTH HOUSTON",
    "SOUTHSIDE PLACE",
    "SPRING VALLEY VILLAGE",
    "TAYLOR LAKE VILLAGE",
    "TOMBALL",
    "WEBSTER",
    "WEST UNIVERSITY PLACE",

    // Census-designated places
    "ALDINE",
    "ATASCOCITA",
    "BARRETT",
    "CHANNELVIEW",
    "CINCO RANCH",
    "CLOVERLEAF",
    "CROSBY",
    "HIGHLANDS",
    "MISSION BEND",
    "SHELDON",
    "SPRING",
    "THE WOODLANDS",

    // Unincorporated communities
    "ALIEF",
    "AIRLINE",
    "BAMMEL",
    "BARKER",
    "BEAUMONT PLACE",
    "BRIDGELAND",
    "CEDAR BAYOU",
    "CHAMPIONS FOREST",
    "CIMARRON",
    "COADY",
    "CYPRESS",
    "DYERSDALE",
    "EAST ALDINE",
    "FALL CREEK",
    "HOCKLEY",
    "HOUMONT PARK",
    "HUFFMAN",
    "HUFSMITH",
    "KINWOOD",
    "KLEIN",
    "KLEINBROOK",
    "KOHRVILLE",
    "LOUETTA",
    "LYNCHBURG",
    "MCNAIR",
    "NORTH HOUSTON",
    "NORTHCLIFFE",
    "NORTHCLIFFE MANOR",
    "NORTHGATE FOREST",
    "REMINGTON RANCH",
    "ROSE HILL",
    "SATSUMA",
    "TRACES",
    "WESTFIELD"
  };
}
