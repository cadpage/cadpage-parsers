package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INFountainCountyParser extends GroupBestParser {

  public INFountainCountyParser() {
    super(new INFountainCountyAParser(),
          new INFountainCountyBParser(),
          new INFountainCountyCParser());
  }


  static final String[] CITY_LIST = new String[]{

      // Cities
      "ATTICA",
      "COVINGTON",

      // Incorporated towns
      "HILLSBORO",
      "KINGMAN",
      "MELLOTT",
      "NEWTOWN",
      "VEEDERSBURG",
      "WALLACE",

      // Unincorporated communities
      "AYLESWORTH",
      "CATES",
      "CENTENNIAL",
      "COAL CREEK",
      "FOUNTAIN",
      "GRAHAM",
      "HARVEYSBURG",
      "LAYTON",
      "RIVERSIDE",
      "ROB ROY",
      "ROBERTS",
      "SILVERWOOD",
      "STEAM CORNER",
      "STONE BLUFF",
      "VINE",
      "YEDDO",

      // Previous settlements
      "STRINGTOWN"
      
      // Warren County
  };

}
