package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;

public class TXMontagueCountyParser extends DispatchA18Parser {

  public TXMontagueCountyParser() {
    super(CITY_LIST, "MONTAGUE COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "crimes.helpdesk@gmail.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BOWIE",
      "NOCONA",
      "ST JO",

      /// Census-designated places
      "MONTAGUE",
      "NOCONA HILLS",
      "RINGGOLD",
      "SUNSET",

      // Other unincorporated communities
      "BELCHERVILLE",
      "BONITA",
      "DYE",
      "FORESTBURG",
      "FRUITLAND",
      "HARDY",
      "ILLINOIS BEND",
      "MALLARD",
      "NEW HARP",
      "SPANISH FORT",
      "STONEBURG",

      // Ghost town
      "CAPPS CORNER",
      "CORINTH",
      "GLADYS",
      "HYNDS CITY",
      "RED RIVER STATION",
      "ROWLAND",
      "SALONA"
  };

}
