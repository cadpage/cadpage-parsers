package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Warren County, OH
 */

public class OHWarrenCountyParser extends GroupBestParser {

  public OHWarrenCountyParser() {
    super(new OHWarrenCountyBParser(),
          new OHWarrenCountyCParser(),
          new OHWarrenCountyDParser(),
          new OHWarrenCountyEParser(),
          new OHWarrenCountyFParser());
  }

  static final String[] CITY_LIST = new String[]{

    // Cities
    "FRANKLIN",
    "LEBANON",
    "LOVELAND",
    "MASON",
    "MIDDLETOWN",
    "MONROE",
    "SPRINGBORO",

    // Villages
    "BLANCHESTER",
    "BUTLERVILLE",
    "CARLISLE",
    "CORWIN",
    "HARVEYSBURG",
    "MAINEVILLE",
    "MORROW",
    "PLEASANT PLAIN",
    "SOUTH LEBANON",
    "WAYNESVILLE",

    // Townships
    "CLEARCREEK",
    "CLEARCREEK TWP",
    "DEERFIELD",
    "DEERFIELD TWP",
    "FRANKLIN",
    "FRANKLIN TWP",
    "HAMILTON",
    "HAMILTON TWP",
    "HARLAN",
    "HARLAN TWP",
    "MASSIE",
    "MASSIE TWP",
    "SALEM",
    "SALEM TWP",
    "SPRING VALLEY TWP",
    "TURTLECREEK",
    "TURTLECREEK TWP",
    "UNION",
    "UNION TWP",
    "WASHINGTON",
    "WASHINGTON TWP",
    "WAYNE",
    "WAYNE TWP",

    // Census-designated places
    "FIVE POINTS",
    "HUNTER",
    "KINGS MILLS",
    "LANDEN",
    "LOVELAND PARK",

    // Unincorporated communities
    "BEEDLES STATION",
    "BLACKHAWK",
    "BLUE BALL (A NEIGHBORHOOD OF MIDDLETOWN)",
    "BROWN'S STORE",
    "CAMARGO",
    "COZADDALE",
    "CROSSWICK",
    "DALLASBURG",
    "DUNLEVY",
    "DODDS",
    "EDWARDSVILLE",
    "FORT ANCIENT",
    "FLAT IRON",
    "FOSTERS",
    "FREDERICKSBURG",
    "GENN TOWN",
    "GREENTREE CORNERS",
    "HAGEMANS CROSSING",
    "HAMMEL",
    "HILLCREST",
    "HENPECK",
    "HICKORY CORNER",
    "HICKS",
    "HOPKINSVILLE",
    "KENDRICKSVILLE",
    "KIRKWOOD",
    "LIBERTY HALL",
    "LEVEL",
    "LYTLE",
    "MATHERS MILL",
    "MIDDLEBORO",
    "MIDDLETOWN JUNCTION",
    "MOUNT HOLLY",
    "MOUNTS STATION",
    "MURDOCH",
    "NEW COLUMBIA",
    "OCEOLA",
    "OREGONIA",
    "OTTERBEIN",
    "PEKIN",
    "RAYSVILLE",
    "RED LION",
    "ROACHESTER",
    "ROSSBURG",
    "RIDGEVILLE",
    "SAN MAR GALE",
    "SENIOR",
    "SCOTTSVILLE",
    "SNIDERCREST",
    "SOCIALVILLE",
    "STUBBS MILL",
    "TWENTY MILE STAND",
    "UNION VILLAGE",
    "UNITY",
    "UTICA",
    "WEST WOODVILLE",
    "WINDSOR",
    "ZOAR",

    // Butler
    "BECKETT RIDGE",
    "MIDDLETOWN",
    "MONROE",
    "OLDE WEST CHESTER",
    "WETHERINGTON",

    "LEMON TWP",
    "LIBERTY TWP",
    "MADISON TWP",
    "UNION TWP",
    "WEST CHESTER TWP",

    // Cleremont County
    "GOSHEN",

    "GOSHEN TWP",
    "MIAMI TWP",
    "WAYNE TWP",

    // Clinton County
    "ADAMS TWP",
    "CHESTER TWP",
    "LIBERTY TWP",
    "LIBERTY TOWNSHIP",
    "VERNON TWP",
    "MARION TWP",

    // Greene County
    "BELLBROOK",
    "FAIRBORN",
    "SPRING VALLEY",

    "CEASARCREEK TWP",
    "SPRING VALLEY TWP",
    "SUGARCREEK TWP",

    // Hamilton County
    "LOCKLAND",
    "LOVELAND",
    "SHARONVILLE",

    "SYMMES TWP",

    // Montgomery County
    "CENTERVILLE",
    "GERMAN TWP",
    "GERMANTOWN",
    "MIAMISBURG",
    "MONTGOMERY",
    "WEST CARROLLTON",

    "WASHINGTON TWP",

  };
}
