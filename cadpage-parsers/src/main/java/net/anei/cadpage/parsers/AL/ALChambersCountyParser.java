package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

/**
 * Chambers County, AL
 */
public class ALChambersCountyParser extends DispatchA74Parser {

  public ALChambersCountyParser() {
    super(CITY_LIST, "CHAMBERS COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "dispatch@chamberscounty911ema.org";
  }

  private static String[] CITY_LIST = new String[]{

      //INCORPORATED
      "CUSSETA",
      "FIVE POINTS",
      "LAFAYETTE",
      "LANETT",
      "VALLEY",
      "WAVERLY",

      //UNINCORPORATED
      "OAK BOWERY",
      "OAKLAND",
      "WHITE PLAINS",

      //CDPS
      "ABANDA",
      "FREDONIA",
      "HUGULEY",
      "PENTON",
      "STANDING ROCK",

      // Troup County, GA
      "WEST POINT"
  };
}
