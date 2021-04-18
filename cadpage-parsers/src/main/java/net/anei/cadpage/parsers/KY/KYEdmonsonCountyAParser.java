package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYEdmonsonCountyAParser extends DispatchA65Parser {
  
  public KYEdmonsonCountyAParser() {
    super(CITY_LIST, "EDMONSON COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@edmonsoncoe911.info";
  }
  
  
  private static final String[] CITY_LIST = new String[]{
      "ASPHALT",
      "BEE SPRING",
      "BIG REEDY",
      "BROWNSVILLE",
      "CEDAR SPRING",
      "CHALYBEATE SPRINGS",
      "HUFF",
      "LINCOLN",
      "LINDSEYVILLE",
      "MAMMOTH CAVE",
      "PIG",
      "RHODA",
      "ROCKY HILL",
      "ROUNDHILL",
      "SUNFISH",
      "SWEEDEN",
      "WINDYVILLE",
      
      //  Barren County
      "PARK CITY",
      
      // Butler County
      "BUTLER CO",
      
      // Hart County
      "CUB RUN",
      
      // Warren County
      "BOWLING GREEN",
      "OAKLAND",
      "PLUM SPRINGS",
      "SMITHS GROVE",
      "WOODBURN"
  };

}
