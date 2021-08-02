package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Christian County, IL
 */
public class ILChristianCountyParser extends DispatchA19Parser {

  public ILChristianCountyParser() {
    super(CITY_CODES, "CHRISTIAN COUNTY", "IL");
  }

  public String getFilter() {
    return "flex@christiancountysheriff.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ASU", "ASSUMPTION",
      "BC",  "BEECHER CITY",
      "BLU", "BLUE MOUND",
      "BUL", "BULPITT",
      "COW", "COWDEN",
      "EDI", "EDINBURG",
      "FAN", "FANCHER",
      "FIN", "FINDLAY",
      "GAY", "GAYS",
      "HAR", "HARVEL",
      "HER", "HERRICK",
      "HEW", "HEWITTVILLE",
      "JEI", "JEISEYVILLE",
      "KIN", "KINCAID",
      "LAN", "LANGELYVILLE",
      "MAC", "MACON",
      "MID", "MIDDLESWORTH",
      "MOD", "MODE",
      "MOR", "MORRISONVILLE",
      "MOW", "MOWEAQUA",
      "MTA", "MT AUBURN",
      "NEE", "NEOGA",
      "NOK", "NOKOMIS",
      "OCO", "OCONEE",
      "OWA", "OWANECO",
      "PAL", "PALMER",
      "PAN", "PANA",
      "PAW", "PAWNEE",
      "ROC", "ROCHESTER",
      "ROT", "ROCHESTER",
      "ROS", "ROSAMOND",
      "SHE", "SHELBYVILLE",
      "SIG", "SIGEL",
      "SPR", "SPRINGFIELD",
      "STE", "STEWARDSON",
      "STO", "STONINGTON",
      "TAY", "TAYLORVILLE",
      "TOV", "TOVEY",
      "TOW", "TOWER HILL",
      "WES", "WESTERVELT",
      "WIN", "WINDSOR"

  });
}
