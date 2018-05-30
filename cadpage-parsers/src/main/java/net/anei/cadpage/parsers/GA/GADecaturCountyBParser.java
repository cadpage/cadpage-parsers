package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GADecaturCountyBParser extends FieldProgramParser {
  
  public GADecaturCountyBParser() {
    super(CITY_LIST, "DECATUR COUNTY", "GA", 
          "ADDR/SXP EMPTY EMPTY EMPTY CALL! INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "dg911@decaturcountyga.gov,2299992137";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split(","), data);
  }
  
  private static final String[] CITY_LIST = new String[]{

      // Cities
      "ATTAPULGUS",
      "BAINBRIDGE",
      "CLIMAX",

      // Town
      "BRINSON",

      // Unincorporated communities
      "AMSTERDAM",
      "AUSMAC",
      "BETHANY",
      "BLACK JACK",
      "CYRENE",
      "ELDORENDO",
      "FACEVILLE",
      "FOWLSTOWN",
      "HANNATOWN",
      "HANOVER",
      "JINKS",
      "LAINGKAT",
      "LYNN STATION",
      "MOUNT PLEASANT",
      "OTISCO",
      "PARKER COURTHOUSE",
      "RECOVERY",
      "SMITHS LANDING",
      "VADA",

      // Ghost town
      "MIRIAM"
  };
}
