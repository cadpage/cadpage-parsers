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
      "BOB WHITE",
      "CECIL JACKSON",
      "COG HILL",
      "HARRIS HAVEN",
      "JEFF THOMAS",
      "LAND LINE",
      "MARTIN LUTHER KING",
      "MEDICAL CENTER",
      "OAK RIDGE",
      "PERSIMMON TREE",
      "PIN OAK",
      "SAMUEL O MOSELEY",
      "ST ANDREWS",
      "ST PHILLIPS"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "CHECK OUT",
      "FALARM",
      "FCHECK",
      "FGAS",
      "FGRASS",
      "FPOWER",
      "FSTRUCTURE",
      "FTEST",
      "FVACC",
      "FVEH",
      "FWOODS",
      "GAS SMELL/LEAK/SPILL",
      "GRASS/ TRASH/LEAF FIRE",
      "MEDICAL ASSISTANCE OF ANY TYPE",
      "POWER LINES/POLES",
      "RESIDENCE/BUSINESS FIRE ALARM",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE SCARLET",
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
