package net.anei.cadpage.parsers.AL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Chambers County, AL
 */
public class ALChambersCountyParser extends DispatchGeoconxParser {
  
  public ALChambersCountyParser() {
    super(CITY_SET, "CHAMBERS COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.org";
  }
  
  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(new String[]{

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
      "STANDING ROCK"
  }));
}
