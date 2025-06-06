package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Harris County, TX
 */
public class TXHarrisCountyParser extends GroupBestParser {

  public TXHarrisCountyParser() {
    super(new TXHarrisCountyAParser(),
          new TXHarrisCountyBParser(),
          new TXHarrisCountyCParser(),
          new TXHarrisCountyFParser(),
          new TXHarrisCountyGParser());
  }

  static final String[] CITY_LIST = new String[]{

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
      "MORGANS POINT",
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
      "WESTFIELD",

      // Brazoria County
      "BRAZORIA COUTY",

      // Cities
      "ALVIN",
      "ANGLETON",
      "BRAZORIA",
      "BROOKSIDE VILLAGE",
      "CLUTE",
      "DANBURY",
      "FREEPORT",
      "IOWA COLONY",
      "LAKE JACKSON",
      "LIVERPOOL",
      "MANVEL",
      "OYSTER CREEK",
      "RICHWOOD",
      "SANDY POINT",
      "SURFSIDE BEACH",
      "SWEENY",
      "WEST COLUMBIA",

      // Towns
      "HOLIDAY LAKES",
      "QUINTANA",
      "VILLAGES",
      "BAILEYS PRAIRIE",
      "BONNEY",
      "HILLCREST",
      "JONES CREEK",

      // Census-designated places
      "DAMON",
      "EAST COLUMBIA",
      "ROSHARON",
      "RYAN ACRES",
      "WILD PEACH VILLAGE",

      // Unincorporated communities
      "AMSTERDAM",
      "ANCHOR",
      "BRAZOSPORT",
      "BRYAN BEACH",
      "CHENANGO",
      "CHINA GROVE",
      "CHOCOLATE BAYOU",
      "DANCIGER",
      "ENGLISH",
      "FOUR CORNERS",
      "HINKLES FERRY",
      "LOCHRIDGE",
      "OLD OCEAN",
      "OTEY",
      "SILVERLAKE",
      "SNIPE",
      "TURTLE COVE",

      // Ghost towns
      "HASIMA",
      "HASTINGS",
      "LAKE BARBARA",
      "MIMS",
      "OAKLAND",
      "PERRYS LANDING",
      "VELASCO",

      // Galveston County
      "GALVESTON COUNTY",

      // Cities
      "BAYOU VISTA",
      "CLEAR LAKE SHORES",
      "DICKINSON",
      "GALVESTON",
      "HITCHCOCK",
      "JAMAICA BEACH",
      "KEMAH",
      "LA MARQUE",
      "SANTA FE",
      "TEXAS CITY",

      // Villages
      "TIKI ISLAND",

      // Census-designated places
      "BACLIFF",
      "BOLIVAR PENINSULA",
      "SAN LEON",
      "UNINCORPORATED COMMUNITIES",
      "ALGOA",
      "BAYVIEW",
      "CAPLEN",
      "CRYSTAL BEACH",
      "GILCHRIST",
      "HIGH ISLAND",
      "PORT BOLIVAR",

      // Fort Bend County
      // Cities
      "ARCOLA",
      "BEASLEY",
      "FULSHEAR",
      "KENDLETON",
      "MEADOWS PLACE",
      "NEEDVILLE",
      "ORCHARD",
      "RICHMOND",
      "ROSENBERG",
      "SIMONTON",
      "SUGAR LAND",
      "WESTON LAKES",

      // Towns
      "THOMPSONS",

      // Villages
      "FAIRCHILDS",
      "PLEAK",

      // Census-designated places
      "CINCO RANCH",
      "CUMINGS",
      "FIFTH STREET",
      "FOUR CORNERS",
      "FRESNO",
      "GREATWOOD",
      "MISSION BEND",
      "PECAN GROVE",
      "SIENNA"
    };
}
