package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNFranklinCountyAParser extends DispatchA74Parser {
  
  public TNFranklinCountyAParser() {
    this("FRANKLIN COUNTY", "TN");
  }
  
  public TNFranklinCountyAParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getAliasCode() {
    return "TNFranklinCounty";
  }

  @Override
  public String getFilter() {
    return "Dispatch@FranklinTN911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
