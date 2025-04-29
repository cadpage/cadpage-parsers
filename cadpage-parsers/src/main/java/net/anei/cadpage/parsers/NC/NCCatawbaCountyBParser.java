package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.FieldProgramParser;

public class NCCatawbaCountyBParser extends FieldProgramParser {

  public NCCatawbaCountyBParser() {
    super(CITY_LIST, "CATAWBA COUNTY", "NC",
          "ALERT:CALL! ADDRESS:ADDRCITY/S CROSS_STREETS:X! CALL_#:ID! UNITS:UNIT! END");
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CLAREMONT",
      "CONOVER",
      "HICKORY",
      "NEWTON",

      // Towns
      "BROOKFORD",
      "CATAWBA",
      "LONG VIEW",
      "MAIDEN",

      // Census-designated places
      "LAKE NORMAN OF CATAWBA",
      "MOUNTAIN VIEW",
      "ST STEPHENS",

      // Unincorporated communities
      "BANOAK",
      "BLACKBURN",
      "DRUMS CROSSROADS",
      "LONG ISLAND",
      "MONBO",
      "OLIVERS CROSSROADS",
      "PROPST CROSSROADS",
      "SHERRILLS FORD",
      "TERRELL",

      // Townships
      "BANDYS TWP",
      "CALDWELL TWP",
      "CATAWBA TWP",
      "CLINES TWP",
      "HICKORY TWP",
      "JACOBS FORK TWP",
      "MOUNTAIN CREEK TWP",
      "NEWTON TWP"
  };

}
