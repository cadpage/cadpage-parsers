package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Monroe County, PA
 */


public class PAMonroeCountyParser extends GroupBestParser {

  public PAMonroeCountyParser() {
    super(new PAMonroeCountyAParser(),
          new PAMonroeCountyBParser(),
          new PAMonroeCountyDParser());
  }


  static final String[] CITY_LIST = new String[]{

      // Boroughs
      "DELAWARE WATER GAP",
      "EAST STROUDSBURG",
      "MOUNT POCONO",
      "STROUDSBURG",

      // Townships
      "BARRETT",
      "CHESTNUTHILL",
      "COOLBAUGH",
      "ELDRED",
      "HAMILTON",
      "JACKSON",
      "MIDDLE SMITHFIELD",
      "PARADISE",
      "POCONO",
      "POLK",
      "PRICE",
      "ROSS",
      "SMITHFIELD",
      "STROUD",
      "TOBYHANNA",
      "TUNKHANNOCK",

      // Census-designated places
      "ARLINGTON HEIGHTS",
      "BRODHEADSVILLE",
      "EFFORT",
      "EMERALD LAKES",
      "GOULDSBORO",
      "INDIAN MOUNTAIN LAKE",
      "MOUNTAINHOME",
      "PENN ESTATES",
      "POCONO LAKE",
      "POCONO PINES",
      "SAW CREEK",
      "SAYLORSBURG",
      "SIERRA VIEW",
      "SUN VALLEY",

      // Unincorporated communities
      "ANALOMINK",
      "APPENZELL",
      "BARTONSVILLE",
      "BLAKESLEE",
      "BOSSARDSVILLE",
      "CANADENSIS",
      "CHERRY VALLEY",
      "CRESCO",
      "GILBERT",
      "GRAVEL PLACE",
      "HAMILTON SQUARE",
      "HENRYVILLE",
      "JONAS",
      "KELLERSVILLE",
      "KEMMERTOWN",
      "KRESGEVILLE",
      "KUNKLETOWN",
      "LONG POND",
      "MARSHALLS CREEK",
      "MCILHANEY",
      "MCMICHAELS",
      "MEISTERTOWN",
      "NEOLA",
      "PARADISE VALLEY",
      "POCONO LAKE",
      "POCONO MANOR",
      "POCONO SUMMIT",
      "REEDERS",
      "SCIOTA",
      "SCOTRUN",
      "SHAWNEE ON DELAWARE",
      "SKYTOP",
      "SNYDERSVILLE",
      "SOUTH STROUDSBURG",
      "SWIFTWATER",
      "TANNERSVILLE",
      "TOBYHANNA",

      // Carbon County
      "TOWAMENSING TOWNSHIP",

      // Luzerne County
      "LEHMAN",

      // Adjacent counties
      "CARBON CO",
      "LACKAWANNA CO",
      "LUZERNE CO",
      "NORTHAMPTON CO",
      "PIKE CO",
      "SUSSEX CO",
      "WARREN CO",
      "WAYNE CO",
  };
}
