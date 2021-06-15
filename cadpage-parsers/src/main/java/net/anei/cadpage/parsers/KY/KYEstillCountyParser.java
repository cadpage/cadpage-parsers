package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYEstillCountyParser extends DispatchA65Parser {

  public KYEstillCountyParser() {
    super(CITY_LIST, "ESTILL COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm3.info,dispatch@EstillKY911.net";
  }

  private static final String[] CITY_LIST = new String[] {

      // City
      "IRVINE",
      "RAVENNA",

      // Other Communities
      "BARNES MOUNTAIN",
      "COBHILL",
      "CRESSY",
      "CRYSTAL",
      "DRIP ROCK",
      "FOX",
      "FURNACE",
      "HARGETT",
      "LEIGHTON",
      "PALMER",
      "PATSEY",
      "PRYSE",
      "RED LICK",
      "SOUTH IRVINE",
      "SPOUT SPRINGS",
      "TIPTON RIDGE",
      "WISEMANTOWN",
      "WINSTON",

      // Morgan County
      "WHITE OAK"
  };

}
