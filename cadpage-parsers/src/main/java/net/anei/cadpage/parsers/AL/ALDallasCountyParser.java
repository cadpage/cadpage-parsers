package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class ALDallasCountyParser extends DispatchA48Parser {

  public ALDallasCountyParser() {
    super(CITY_LIST, "DALLAS COUNTY", "AL", FieldType.X, A48_NO_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "@bellsouth.net,dallasco911.donotreply@gmail.com";
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "BARRETT CREEK",
      "BIG PINE",
      "BOB WHITE",
      "CADES COVE",
      "CECIL JACKSON",
      "CEOLA L MILLER",
      "COG HILL",
      "DEEP WOODS",
      "EARL GOODWIN",
      "HARRIS HAVEN",
      "J L CHESTNUT JR",
      "JEFF THOMAS",
      "L L ANDERSON",
      "LAKE LANIER",
      "LAKEWOOD ESTATES",
      "LAND LINE",
      "MARTIN LUTHER KING",
      "MEDICAL CENTER",
      "MEL BAILEY",
      "NORTH LAKE",
      "OAK RIDGE",
      "PERSIMMON TREE",
      "PIN OAK",
      "QUAIL RIDGE",
      "SAMUEL O MOSELEY",
      "SCARLET OAK",
      "SELMA SQUARE APT",
      "ST ANDREWS",
      "ST PHILLIPS",
      "WEST PARK"
      };

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP/ UNABLE TO RECONNECT",
      "ACCIDENT NO INJURIES",
      "ACCIDENT WITH INJURIES",
      "AMBULA",
      "AMBULANCE TRANSFER",
      "ANIMAL COMPLAINT",
      "ASSAULT ALREADY OCCURRED",
      "ASSIST MOTORIST/CITIZEN",
      "ATTEMPT TO CONTACT WELFARE CHECK",
      "BREAKING ENTERING VEH IN PROGRESS",
      "CHECK OUT",
      "CIVIL MATTER BOTH PARTIES THERE",
      "CIVIL MATTER ONLY ONE PARTY",
      "CRIMINAL MISCHIEF ALREADY OCCURED",
      "DISORDERLY CONDUCT",
      "ESCORT OF ANY KIND",
      "FALARM",
      "FCHECK",
      "FGAS",
      "FGRASS",
      "FIGHT IN PROGRESS",
      "FPOWER",
      "FSTRUCTURE",
      "FTEST",
      "FVACC",
      "FVEH",
      "FWOODS",
      "GAS SMELL/LEAK/SPILL",
      "GRASS/ TRASH/LEAF FIRE",
      "HARASSMENT ALREADEY OCCURRED",
      "HARASSMENT IN PROGRESS",
      "JUVENILE COMPLAINT",
      "MEDICAL ASSISTANCE OF ANY TYPE",
      "MENTAL PERSON",
      "MISSING PERSON",
      "PACT1",
      "POWER LINES/POLES",
      "PROPERTY DAMAGED",
      "RECKLESS DRIVING",
      "RECKLESS ENGANGERMENT/MENACING",
      "RESIDENCE/BUSINESS FIRE ALARM",
      "ROAD BLOCK DEBRIS/HAZARD",
      "SEE THE COMPLAINANT",
      "SEXUAL OFFENSE ALREADY OCCURRED",
      "SHOOTING W/VICTIM",
      "SHOTS FIRED NO VICTIM",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE SCARLET",
      "SUSPICIOUS PERSON/VEHICLE",
      "THEFT ALREADY OCCURRED",
      "UNWANTED GUEST",
      "UNSECURE BUSINESS/RESIDENCE",
      "USED FOR TESTING",
      "VEHICLE ACCIDENT",
      "VEHICLE FIRE",
      "WOODS FIRE"
  );

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "SELMA",
      "VALLEY GRANDE",

      // Towns
      "ORRVILLE",

      // Census-designated places
      "SELMONT-WEST SELMONT",

      // Unincorporated communities
      "BELOIT",
      "BOGUE CHITTO",
      "BROWNS",
      "CARLOWVILLE",
      "CRUMPTONIA",
      "ELM BLUFF",
      "HARRELL",
      "MANILA",
      "MARION JUNCTION",
      "MINTER",
      "PLANTERSVILLE",
      "PLEASANT HILL",
      "RICHMOND",
      "SAFFORD",
      "SARDIS",
      "SUMMERFIELD",
      "TYLER",

      // Ghost town
      "CAHABA",

      // Autauga County
      "JONES"
  };
}
