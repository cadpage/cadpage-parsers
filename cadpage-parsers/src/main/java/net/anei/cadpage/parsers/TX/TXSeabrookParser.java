package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Seabrook , TX
 */

public class TXSeabrookParser extends GroupBestParser {

  public TXSeabrookParser() {
    super(new TXSeabrookAParser(), new TXSeabrookBParser());
  }


  static String[] CITY_LIST = new String[]{

    // Cities
    "BAYTOWN",
    "BELLAIRE",
    "BUNKER HILL VILLAGE",
    "DEER PARK",
    "EL LAGO",
    "FRIENDSWOOD",
    "GALENA PARK",
    "HEDWIG VILLAGE",
    "HILSHIRE VILLAGE",
    "HOUSTON",
    "HUMBLE",
    "HUNTERS CREEK VILLAGE",
    "JACINTO CITY",
    "JERSEY VILLAGE",
    "KATY",
    "LA PORTE",
    "LEAGUE CITY",
    "MISSOURI CITY",
    "MORGANS POINT",
    "NASSAU BAY",
    "PASADENA",
    "PEARLAND",
    "PINEY POINT VILLAGE",
    "SEABROOK",
    "SHOREACRES",
    "SOUTH HOUSTON",
    "SOUTHSIDE PLACE",
    "SPRING",
    "SPRING VALLEY VILLAGE",
    "STAFFORD",
    "TAYLOR LAKE VILLAGE",
    "TOMBALL",
    "WALLER",
    "WEBSTER",
    "WEST UNIVERSITY PLACE",

    //Census-designated places
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
    "SPRING"
  };

}
