package net.anei.cadpage.parsers.MA;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class MAHampdenCountyParser extends DispatchA32Parser {

  public MAHampdenCountyParser() {
    super(CITY_LIST, "HAMPDEN COUNTY", "MA");
  }

  @Override
  public String getFilter() {
    return "wilbrahamrecc@gmail.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "AGAWAM",
      "CHICOPEE",
      "HOLYOKE",
      "PALMER",
      "SPRINGFIELD",
      "WEST SPRINGFIELD",
      "WESTFIELD",

      // Towns
      "BLANDFORD",
      "BRIMFIELD",
      "CHESTER",
      "EAST LONGMEADOW",
      "GRANVILLE",
      "HAMPDEN",
      "HOLLAND",
      "LONGMEADOW",
      "LUDLOW",
      "MONSON",
      "MONTGOMERY",
      "RUSSELL",
      "SOUTHWICK",
      "TOLLAND",
      "WALES",
      "WILBRAHAM",

      // Census-designated places
      "BLANDFORD",
      "CHESTER",
      "HOLLAND",
      "MONSON CENTER",
      "RUSSELL",
      "WILBRAHAM",

      // Other unincorporated communities
      "BONDSVILLE",
      "DEPOT VILLAGE",
      "FEEDING HILLS",
      "THREE RIVERS",
      "WORONOCO"
  };

}
