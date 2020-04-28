package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCNashCountyParser extends GroupBestParser {
  
  public NCNashCountyParser() {
    super(new NCNashCountyAParser(), new NCNashCountyBParser(),
          new NCNashCountyCParser());
  }
  
  static final String[] CITY_LIST = new String[]{
    
      //  Cities
      "BAILEY",
      "CASTALIA",
      "CASTLIA",          // Typo
      "DORTCHES",
      "ELM CITY",
      "MIDDLESEX",
      "MOMEYER",
      "NASHVILLE",
      "RED OAK",
      "ROCKY MOUNT",
      "SHARPSBURG",
      "SHARPSBURGDIS",
      "SPRING HOPE",
      "WHITAKERS",
      "ZEBULON",
      
      // Townships
      "BAILEY",
      "BATTLEBORO",
      "CASTALIA",
      "COOPERS",
      "DRY WELLS",
      "FERRELLS",
      "GRIFFINS",
      "JACKSON",
      "MANNINGS",
      "NASHVILLE",
      "NORTH WHITAKERS",
      "OAK LEVEL",
      "RED OAK",
      "ROCKY MOUNT",
      "SPRING HOPE",
      "SOUTH WHITAKERS",
      "STONY CREEK",
      
      // Franklin County
      "BUNN",
      "LOUISBURG",
      
      // Halifax county
      "AURELIAN SPRINGS",
      "BRINKLEYVILLE",
      "HEATHSVILLE",
      "ENFIELD",
      "HALIFAX",
      "HOBGOOD",
      "LITTLETON",
      "HOLLISTER",
      "ROANOKE RAPIDS",
      "SCOTLAND NECK",
      "SOUTH ROSEMARY",
      "SOUTH WELDON",
      "WELDON",

      "BUTTERWOOD",
      "CONOCONNARA",
      "FAUCETT",
      "PALMYRA",
      "ROSENEATH",
      
      // Wilson County
      "ELM CITY",
      "SIMS",
      "SIMMS",       // Misspelled
      "WILSON",
      
      // Counties
      "EDGECOMBE CO",
      "FRANKLIN CO",
      "JOHNSTON CO",
      "HALIFAX CO",
      "WARREN CO",
      "WILSON CO",
      
      // ???
      "VIRGINIA"
      
  };
  
  static final Properties CITY_FIXES = buildCodeTable(new String[]{
      "CASTLIA",       "CASTALIA",
      "EDGECOMBE CO",  "EDGECOMBE COUNTY",
      "SHARPSBURGDIS", "SHARPSBURG",
      "SIMMS",         "SIMS"
  });

}
