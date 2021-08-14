package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCEdgecombeCountyParser extends GroupBestParser {

  public NCEdgecombeCountyParser() {
    super(new NCEdgecombeCountyAParser(),
          new NCEdgecombeCountyBParser(),
          new NCEdgecombeCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{
    "BATTLEBORO",
    "CONETOE",
    "LEGGETT",
    "MACCLESFIELD",
    "PINETOPS",
    "PRINCEVILLE",
    "ROCKY MOUNT",
    "SHARPSBURG",
    "SOUTH WHITAKERS",
    "SPEED",
    "TARBORO",
    "WHITAKERS",
    "HOUSEVILLE",

    "TARBORO TWP",
    "LOWER CONETOE TWP",
    "UPPER CONETOE TWP",
    "DEEP CREEK TWP",
    "LOWER FISHING CREEK TWP",
    "UPPER FISHING CREEK TWP",
    "SWIFT CREEK TWP",
    "SPARTA TWP",
    "OTTER CREEK TWP",
    "LOWER TOWN CREEK TWP",
    "WALNUT CREEK TWP",
    "ROCKY MOUNT TWP",
    "COKEY TWP",
    "UPPER TOWN CREEK TWP",

    // Hallifax County
    "HOBGOOD",

    // Nash County
    "NASHVILLE",

    "BAILEY",
    "CASTALIA",
    "GOLD ROCK",
    "GOLDROCK",
    "DORTCHES",
    "MIDDLESEX",
    "MOMEYER",
    "RED OAK",
    "SPRING HOPE",
    "ZEBULON",

    "CORINTH",

    "BAILEY TWP",
    "BATTLEBORO TWP",
    "CASTALIA TWP",
    "COOPERS TWP",
    "DRY WELLS TWP",
    "FERRELLS TWP",
    "GRIFFINS TWP",
    "JACKSON TWP",
    "MANNINGS TWP",
    "NASHVILLE TWP",
    "NORTH WHITAKERS TWP",
    "OAK LEVEL TWP",
    "RED OAK TWP",
    "SPRING HOPE TWP",

    // Pitt County
    "FOUNTAIN",
    "GREENVILLE",

    // Wilson County
    "ELM CITY",

    // Wayne County
    "STONY CREEK",
  };
}
