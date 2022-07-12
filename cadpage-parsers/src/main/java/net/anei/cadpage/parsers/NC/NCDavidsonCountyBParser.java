package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class NCDavidsonCountyBParser extends DispatchH02Parser {
  
  public NCDavidsonCountyBParser() {
    super(CITY_CODES, "DAVIDSON COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "@DAVIDSONCOUNTYNC.GOV";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARC",  "ARCHDALE",     // Guildford County
      "CL",   "CLEMMONS",     // FOrsyth County
      "DEN",  "DENTON",
      "GREE", "GREENSBORO",
      "HP",   "HIGH POINT",
      "KV",   "KERNERSVILLE", // Forsyth County
      "LEX",  "LEXINGTON", 
      "LIN",  "LINWOOD",
      "NL",   "NEW LONDON",   // Stanly County
      "RAN",  "RANDOLPH COUNTY",
      "RWC",  "ROWAN COUNTY",
      "THA",  "THOMASVILLE",
      "TROY", "TROY",         // Montgomery County
      "WELC", "WELCOME",
      "WS",   "WINSTON-SALEM" // Forsyth County
      // Missing MIDWAY and WALLBURG

  });
}
