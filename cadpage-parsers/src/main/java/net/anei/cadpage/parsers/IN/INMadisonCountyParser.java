package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INMadisonCountyParser extends GroupBestParser {

  public INMadisonCountyParser() {
    super(new INMadisonCountyAParser(),
          new INMadisonCountyBParser(),
          new INMadisonCountyCParser(),
          new INMadisonCountyEParser());
  }

  static final String[] CITY_LIST = new String[]{

      // Cities and towns
      "ALEXANDRIA",
      "ANDERSON",
      "CHESTERFIELD",
      "COUNTRY CLUB HEIGHTS",
      "EDGEWOOD",
      "ELWOOD",
      "FRANKTON",
      "INGALLS",
      "LAPEL",
      "MARKLEVILLE",
      "ORESTES",
      "PENDLETON",
      "RIVER FOREST",
      "SUMMITVILLE",
      "WOODLAWN HEIGHTS",

      // Unincorporated towns
      "ALFONT",
      "ALLIANCE",
      "BLOOMER",
      "COLLEGE CORNER",
      "DUNDEE",
      "EDGEWOOD VILLAGE",
      "EMPORIA",
      "FISHERSBURG",
      "FLORIDA",
      "GEHRING AND GUMZ DITCH",
      "GIMCO CITY",
      "HAMILTON",
      "HARDSCRABBLE",
      "HUNTSVILLE",
      "IDLEWOLD",
      "LEISURE",
      "LINWOOD",
      "MOONSVILLE",
      "NEW COLUMBUS",
      "NORTH ANDERSON",
      "PERKINSVILLE",
      "PROSPERITY",
      "RIGDON",
      "SOUTH ELWOOD",
      "SUNVIEW",

      // Townships
      "ADAMS",
      "ANDERSON",
      "BOONE",
      "DUCK CREEK",
      "FALL CREEK",
      "GREEN",
      "JACKSON",
      "LAFAYETTE",
      "MONROE",
      "PIPE CREEK",
      "RICHLAND",
      "STONY CREEK",
      "UNION",
      "VAN BUREN",

      // Delaware County
      "DALEVILLE",
      "GASTON",

      // Grant County
      "FAIRMOUNT",

      // Hamilton County
      "ATLANTA",
      "NOBLESVILLE",

      // Henry County
      "MIDDLETOWN",

      // Hancock County
      "FORTVILLE"
  };

}
