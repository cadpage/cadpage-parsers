package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchX01Parser;

public class NCDavidsonCountyCParser extends DispatchX01Parser {

  public NCDavidsonCountyCParser() {
    super(CITY_CODES, "DAVIDSON COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ALS",  "ALSEA",
      "ARC",  "ARCHDALE",
      "BLA",  "BLACHLY",
      "BLU",  "BLUE RIVER",
      "CAS",  "CASCADOA",
      "CHE",  "CHESHIRE",
      "CHGV", "CHINA GROVE",
      "CL",   "CLEMMONS",
      "CLEV", "CLEVELAND",
      "CLVD", "CLEVELAND",
      "COB",  "COBURG",
      "COOL", "COOLEEMEE",
      "COT",  "COTTAGE GROVE",
      "CRE",  "CRESWELL",
      "CRT",  "CRESCENT LAKE",
      "DEA",  "DEADWOOD",
      "DEN",  "DENTON",
      "DEX",  "DEXTER",
      "DOR",  "DORENA",
      "DRA",  "DRAIN",
      "ELM",  "ELMIRA",
      "ESPN", "EAST SPENCER",
      "EUG",  "EUGENE",
      "FAL",  "FALL CREEK",
      "FATH", "FAITH",
      "FLO",  "FLORENCE",
      "GOLD", "GOLD HILL",
      "GREE", "GREENSBORO",
      "GRQY", "GRANITE QUARRY",
      "HAL",  "HALSEY",
      "HAR",  "HARRISBURG",
      "HP",   "HIGH POINT",
      "IDA",  "IDANHA",
      "JAS",  "JASPER",
      "JUN",  "JUNCTION CITY",
      "KANN", "KANNAPOLIS",
      "KV",   "KERNERSVILLE",
      "LAND", "LANDIS",
      "LEA",  "LEABURG",
      "LEX",  "LEXINGTON",
      "LIN",  "LINWOOD",
      "LOR",  "LORANE",
      "LOW",  "LOWELL",
      "MAP",  "MAPLETON",
      "MAR",  "MARCOLA",
      "MCK",  "MCKENZIE BRIDGE",
      "MOCK", "MOCKSVILLE",
      "MON",  "MONROE",
      "MOOR", "MOORESVILLE",
      "MTUL", "MT ULLA",
      "NL",   "NEW LONDON",
      "NOT",  "NOTI",
      "OAK",  "OAKRIDGE",
      "PLE",  "PLEASANT HILL",
      "RAN",  "RANDOLPH COUNTY",
      "RICH", "RICHFIELD",
      "ROCK", "ROCKWELL",
      "RWC",  "ROWAN COUNTY",
      "SALS", "SALISBURY",
      "SIS",  "SISTERS",
      "SPEN", "SPENCER",
      "SPR",  "SPRINGFIELD",
      "SWI",  "SWISSHOME",
      "THA",  "THOMASVILLE",
      "TID",  "TIDEWATER",
      "TROY", "TROY",
      "VEN",  "VENETA",
      "VID",  "VIDA",
      "WAN",  "WALTON",
      "WEF",  "WESTFIR",
      "WEL",  "WESTLAKE",
      "WELC", "WELCOME",
      "WOOD", "WOODLEAF",
      "WS",   "WINSTON-SALEM",
      "YAC",  "YACHATS"
  });
}
