package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Grafton County, NH
*/


public class NHGraftonCountyParser extends GroupBestParser {

  public NHGraftonCountyParser() {
    super(new NHGraftonCountyAParser(), new NHGraftonCountyBParser(),
          new NHGraftonCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{

    "GRAFTON COUNTY",

    // Cities
    "LEBANON",

    // Towns
    "ALEXANDRIA",
    "ASHLAND",
    "BATH",
    "BENTON",
    "BETHLEHEM",
    "BRIDGEWATER",
    "BRISTOL",
    "CAMPTON",
    "CANAAN",
    "DORCHESTER",
    "EASTON",
    "ELLSWORTH",
    "ENFIELD",
    "FRANCONIA",
    "GRAFTON",
    "GROTON",
    "HANOVER",
    "HAVERHILL",
    "HEBRON",
    "HOLDERNESS",
    "LANDAFF",
    "LINCOLN",
    "LISBON",
    "LITTLETON",
    "LYMAN",
    "LYME",
    "MONROE",
    "ORANGE",
    "ORFORD",
    "PIERMONT",
    "PLYMOUTH",
    "RUMNEY",
    "SUGAR HILL",
    "SUGARHILL",
    "THORNTON",
    "WARREN",
    "WATERVILLE VALLEY",
    "WENTWORTH",
    "WOODSTOCK",

    // Township
    "LIVERMORE",
    "LIVERMORE TWP",

    // Census-designated places
    "ASHLAND",
    "BETHLEHEM",
    "BRISTOL",
    "CANAAN",
    "ENFIELD",
    "HANOVER",
    "LINCOLN",
    "LISBON",
    "LITTLETON",
    "MOUNTAIN LAKES",
    "NORTH HAVERHILL",
    "NORTH WOODSTOCK",
    "PLYMOUTH",
    "WOODSVILLE",

    // Villages
    "EAST HEBRON",
    "ENFIELD CENTER",
    "ETNA",
    "GLENCLIFF",
    "LYME CENTER",
    "MONTCALM",
    "PIKE",
    "STINSON LAKE",
    "WEST LEBANON",


    // Carroll County
    "CARROLL COUNTY",

    // Cities
      "ALBANY",
      "BARTLETT",
      "BROOKFIELD",
      "CHATHAM",
      "CONWAY",
      "EATON",
      "EFFINGHAM",
      "FREEDOM",
      "HART'S LOCATION",
      "JACKSON",
      "MADISON",
      "MOULTONBOROUGH",
      "OSSIPEE",
      "SANDWICH",
      "TAMWORTH",
      "TUFTONBORO",
      "WAKEFIELD",
      "WOLFEBORO",

  //Township
      "HALE'S LOCATION",

  //Census-designated places
      "BARTLETT",
      "CENTER OSSIPEE",
      "CENTER SANDWICH",
      "CONWAY",
      "MELVIN VILLAGE",
      "NORTH CONWAY",
      "SANBORNVILLE",
      "SUISSEVALE",
      "UNION",
      "WOLFEBORO",

  //Villages
      "CENTER CONWAY",
      "CHOCORUA",
      "EAST WAKEFIELD",
      "EIDELWEISS",
      "FERNCROFT",
      "GLEN",
      "INTERVALE",
      "KEARSARGE",
      "LEES MILL",
      "MIRROR LAKE",
      "NORTH SANDWICH",
      "REDSTONE",
      "SILVER LAKE",
      "SOUTH TAMWORTH",
      "WEST OSSIPEE",
      "WOLFEBORO FALLS",
      "WONALANCET",


      "CALEDONIA COUNTY",

      // Towns
      "BARNET",
      "BURKE",
      "DANVILLE",
      "GROTON",
      "HARDWICK",
      "KIRBY",
      "LYNDON",
      "NEWARK",
      "PEACHAM",
      "RYEGATE",
      "SHEFFIELD",
      "ST JOHNSBURY",
      "STANNARD",
      "SUTTON",
      "WALDEN",
      "WATERFORD",
      "WHEELOCK",

      // Villages
      "BURKE HOLLOW",
      "EAST HARDWICK",
      "EAST LYNDON",
      "EAST RYEGATE",
      "EAST ST JOHNSBURY",
      "HARDWICK",
      "LOWER WATERFORD",
      "LYNDON CENTER",
      "LYNDON CORNER",
      "LYNDONVILLE",
      "MACKVILLE",
      "MCINDOE FALLS",
      "PASSUMPSIC",
      "RYEGATE CORNER",
      "SOUTH KIRBY",
      "SOUTH RYEGATE",
      "ST JOHNSBURY CENTER",
      "UPPER WATERFORD",
      "WEST BURKE",

      // Census-designated places
      "EAST BURKE",

      // Coos County
      "CARROL",
      "CARROLL",
      "DALTON",
      "JEFFERSON",
      "WHITEFIELD",

      // Sullivan County
      "CLAREMONT",
      "GRANTHAM",
      "PLAINFIELD",
      "UNITY",

      // Merimack County
      "BRADFORD",
      "NEWBURY",
      "NORTHUMBERLAND",

      // Essex County, VT
      "LUNENBURG",

      // Orange County, VT
      "W NEWBURY",
      "WELLS RIVER",
      "WEST NEWBURY",

      // Windsor County, VT
      "HARTFORD"
  };

}
