package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILKaneCountyParser extends GroupBestParser {
  
  public ILKaneCountyParser() {
    super(new ILKaneCountyAParser(), new ILKaneCountyBParser(), new ILKaneCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{
    
      "KANE CO",

      // Townships
      "AURORA TWP",
      "BATAVIA TWP",
      "BIG ROCK TWP",
      "BLACKBERRY TWP",
      "BURLINGTON TWP",
      "CAMPTON TWP",
      "DUNDEE TWP",
      "ELGIN TWP",
      "GENEVA TWP",
      "HAMPSHIRE TWP",
      "KANEVILLE TWP",
      "PLATO TWP",
      "RUTLAND TWP",
      "ST CHARLES TWP",
      "SUGAR GROVE TWP",
      "VIRGIL TWP",
      
      // Cities and Towns
      "ALGONQUIN",
      "AURORA",
      "BARRINGTON HILLS",
      "BARTLETT",
      "BATAVIA",
      "BIG ROCK",
      "BURLINGTON",
      "CAMPTON HILLS",
      "CARPENTERSVILLE",
      "DUNDEE",
      "EAST DUNDEE",
      "ELBURN",
      "ELGIN",
      "GENEVA",
      "GILBERTS",
      "HAMPSHIRE",
      "HOFFMAN ESTATES",
      "HUNTLEY",
      "KANEVILLE",
      "LAKE MARIAN",
      "LILY LAKE",
      "MAPLE PARK",
      "MONTGOMERY",
      "NORTH AURORA",
      "PINGREE GROVE",
      "SOUTH BARRINGTON",
      "ST CHARLES",
      "SLEEPY HOLLOW",
      "SOUTH ELGIN",
      "SUGAR GROVE",
      "VIRGIL",
      "WAYNE",
      "WEST DUNDEE",
      
      // Unicorporated Communities
      "ALLENS CORNERS",
      "LA FOX",
      "MOOSEHEART",
      "PLATO CENTER",
      "STARKS, ILLINOIS",
      "WASCO",
      
      // Cook County
      "BARRINGTON",
      "SCHAUMBURG",
      "STREAMWOOD"

  };

}
