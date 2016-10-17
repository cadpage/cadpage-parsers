package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA50Parser;


public class MIKalamazooCountyParser extends DispatchA50Parser {
  
  public MIKalamazooCountyParser() {
    super(CITY_CODES, "KALAMAZOO COUNTY", "MI");
    }
  
  @Override
  public String getFilter() {
    return "kdpshelpdesk@kalamazoocity.org";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALAM",  "ALAMO TWP",
      "AUGU",  "AUGUSTA",
      "BRAD",  "BRADY TWP",
      "CHAR",  "CHARLESTON TWP",
      "CLXT",  "CLIMAX TWP",
      "CLXV",  "CLIMAX",
      "COMS",  "COMSTOCK TWP",
      "COOP",  "COOPER TWP",
      "GALE",  "GALESBURG",
      "KALC",  "KALAMAZOO",
      "KALT",  "KALAMAZOO TWP",
      "OSHT",  "OSHTEMO TWP",
      "PARC",  "PARCHMENT",
      "PAVI",  "PAVILION TWP",
      "PORT",  "PORTAGE",
      "PRRO",  "PRAIRIE RONDE TWP",
      "RICT",  "RICHLAND TWP",
      "RICV",  "RICHLAND",
      "ROSS",  "ROSS TWP",
      "SCHT",  "SCHOOLCRAFT TWP",
      "SCHV",  "SCHOOLCRAFT",
      "TEXA",  "TEXAS TWP",
      "VICK",  "VICKSBURG",
      "WAKE",  "WAKESHMA TWP"
  });
}
