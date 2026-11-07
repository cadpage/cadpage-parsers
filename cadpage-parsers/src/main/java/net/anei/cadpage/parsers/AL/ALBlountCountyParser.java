package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALBlountCountyParser extends GroupBestParser {

  public ALBlountCountyParser() {
    super(new ALBlountCountyAParser(),
          new ALBlountCountyBParser(),
          new ALBlountCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{

    // Cities
    "LEEDS",
    "ONEONTA",
    "WARRIOR",

    // Towns
    "ALLGOOD",
    "ALTOONA",
    "BLOUNTSVILLE",
    "CLEVELAND",
    "COUNTY LINE",
    "GARDEN CITY",
    "HAYDEN",
    "HIGHLAND LAKE",
    "LOCUST FORK",
    "NECTAR",
    "ROSA",
    "SNEAD",
    "SUSAN MOORE",
    "TRAFFORD",

    // Census-designated place
    "SMOKE RISE",

    // Unincorporated communities
    "BANGOR",
    "BLOUNT SPRINGS",
    "BRIGHT STAR",
    "BROOKSVILLE",
    "HOPEWELL",
    "LITTLE WARRIOR",
    "MOUNT HIGH",
    "NECTAR",
    "REMLAP",
    "SKY BALL",
    "STRAIGHT MOUNTAIN",
    "SUMMIT",

    // Cullman COunty
    "CULLMAN",

    // Marshall County
    "ARAB",
    "BOAZ",
    "GUNTERSVILLE",
    "HORTON"
  };
}
