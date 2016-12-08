package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class INGibsonCountyParser extends DispatchH02Parser {
  
  public INGibsonCountyParser() {
    super(CITY_CODES, "GIBSON COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "SHERIFFOFFICE@GIBSONCOUNTY-IN.GOV";
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
  
      "FTB", "FORT BRANCH",
      "HAU", "HAUBSTADT",      
      "POS", "POSEYVILLE"

  });
}
