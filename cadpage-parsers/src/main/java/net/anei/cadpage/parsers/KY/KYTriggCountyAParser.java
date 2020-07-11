package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

/**
 * Trigg County, KY
 */
public class KYTriggCountyAParser extends DispatchA65Parser {
  
  public KYTriggCountyAParser() {
    super(CITY_LIST, "TRIGG COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // City
      "CADIZ",

      // Census-designated place
      "CERULEAN",

      // Other unincorporated places
      "CALEDONIA",
      "CANTON",
      "LINTON",
      "ROARING SPRING",
      "ROCKCASTLE",
      "WALLONIA"
  };
}
