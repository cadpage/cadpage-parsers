package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Christian County, IL
 */
public class ILShelbyCountyParser extends DispatchA19Parser {

  public ILShelbyCountyParser() {
    super(CITY_CODES, "SHELBY COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "flex@christiancountysheriff.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ASU",  "ASSUMPTION",
//      "BC",   "",
      "BEE",  "BEECHER CITY",
      "BET",  "BETHANY",
      "BLU",  "BLUE MOUND",
      "COW",  "COWDEN",
      "FIN",  "FINDLAY",
      "HER",  "HERRICK",
      "LAK",  "LAKEWOOD",
      "MOD",  "MODE",
      "MOW",  "MOWEAQUA",
      "OCO",  "OCONEE TWP",
      "PAN",  "PANA",
      "SHE",  "SHELBYVILLE",
      "STE",  "STEWARDSON",
      "STO",  "STONINGTON TWP",
      "SUL",  "SULLIVAN",
      "TAY",  "TAYLORVILLE",
      "WIN",  "WINDSOR TWP"
  });

  /**
1596 E 50 N

CERRO GORDO
BLANDINSVILLE
HERRICK, SHELBY COUNTY
EFFINGHAM, EFFINGHAM COUNTY

1650 E 50TH RD

MENDOTA
ARTHUR, DOUGLAS COUNTY
SEYMOUR
FARINA, FAYETTE/MARION COUNTY
CONGERVILLE
   */
}
