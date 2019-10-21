package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNFranklinCountyParser extends DispatchA74Parser {
  
  public TNFranklinCountyParser() {
    this("FRANKLIN COUNTY", "TN");
  }
  
  public TNFranklinCountyParser(String defCity, String defState) {
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
