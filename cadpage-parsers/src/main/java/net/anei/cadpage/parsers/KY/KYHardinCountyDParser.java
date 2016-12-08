package net.anei.cadpage.parsers.KY;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


public class KYHardinCountyDParser extends DispatchA9Parser {
  
  public KYHardinCountyDParser() {
    super(CITY_CODES, "HARDIN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "NewWorld@Radcliff.org";
  }
  
  static final Properties CITY_CODES = buildCodeTable(new String[]{
      
   "CTY",    "RADCLIFF",
   "RAD",    "RADCLIFF",   
   "VG",     "VINE GROVE",
   
  });
}
