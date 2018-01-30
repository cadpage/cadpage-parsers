package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class NHCarrollCountyParser extends DispatchA32Parser {
  
  public NHCarrollCountyParser() {
    super(CITY_LIST, "CARROLL COUNTY", "NH");
  }
  
  private static final String[] CITY_LIST = new String[]{

  //Cities
      
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
      "WONALANCET"
  };

}
