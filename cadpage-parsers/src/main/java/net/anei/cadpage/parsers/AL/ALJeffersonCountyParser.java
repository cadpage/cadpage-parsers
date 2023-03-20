package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


public class ALJeffersonCountyParser extends GroupBestParser {

  public ALJeffersonCountyParser() {
    super(new ALJeffersonCountyBParser(),
          new ALJeffersonCountyCParser(), new ALJeffersonCountyDParser(),
          new ALJeffersonCountyEParser(), new ALJeffersonCountyFParser(),
          new ALJeffersonCountyGParser(), new ALJeffersonCountyHParser(),
          new ALJeffersonCountyIParser(), new ALJeffersonCountyJParser(),
          new ALJeffersonCountyKParser(), new ALJeffersonCountyLParser(),
          new ALJeffersonCountyMParser(),

          // The ALJeffersionCountyA parser is highly promiscuous
          // so we only check it after everything else has been tried
          new GroupBlockParser(),         new ALJeffersonCountyAParser());
  }


  static final String[] CITY_LIST = new String[]{
      //Cities
      "ADAMSVILLE",
      "BESSEMER",
      "BIRMINGHAM",
      "BRIGHTON",
      "CENTER POINT",
      "CLAY",
      "FAIRFIELD",
      "FULTONDALE",
      "GARDENDALE",
      "GRAYSVILLE",
      "HELENA",
      "HOMEWOOD",
      "HOOVER",
      "HUEYTOWN",
      "IRONDALE",
      "KIMBERLY",
      "LEEDS",
      "LIPSCOMB",
      "MIDFIELD",
      "MOUNTAIN BROOK",
      "PINSON",
      "PLEASANT GROVE",
      "SUMITON",
      "TARRANT",
      "TRUSSVILLE",
      "VESTAVIA HILLS",
      "WARRIOR",

      //Towns
      "ARGO",
      "BROOKSIDE",
      "CARDIFF",
      "COUNTY LINE",
      "MAYTOWN",
      "MORRIS",
      "MULGA",
      "NORTH JOHNS",
      "SYLVAN SPRINGS",
      "TRAFFORD",
      "WEST JEFFERSON",

      //Census-designated places
      "CHALKVILLE",
      "CONCORD",
      "EDGEWATER",
      "FORESTDALE",
      "GRAYSON VALLEY",
      "MCDONALD CHAPEL",
      "MINOR",
      "MOUNT OLIVE",
      "ROCK CREEK",

      //Unincorporated communities

      "ADGER",
      "ALTON",
      "COALBURG",
      "CORNER",
      "CRUMLEY CHAPEL",
      "DOCENA",
      "DOLOMITE",
      "FLAT TOP",
      "HOPEWELL",
      "KIMBRELL",
      "MCCALLA",
      "NEW CASTLE",
      "PALMERDALE",
      "SAYRE",
      "SHANNON",
      "WATSON",
  };

}
