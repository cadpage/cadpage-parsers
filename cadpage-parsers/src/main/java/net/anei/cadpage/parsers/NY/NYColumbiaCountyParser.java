package net.anei.cadpage.parsers.NY;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;


public class NYColumbiaCountyParser extends DispatchA13Parser {
  
  public NYColumbiaCountyParser() {
    super(CITY_CODES, "COLUMBIA COUNTY", "NY");
  }
  
  // Override checkAddress to relax the standards a bit
  @Override
  protected boolean isValidAddress(String address) {
    return super.checkAddress(address, 1) > STATUS_NOTHING;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CANA", "CANAAN",
      "CLAV", "CLAVERACK",
      "COPA", "COPAKE",
      "GHEN", "GHENT", 
      "GREE", "GREENPORT",
      "HILL", "HILLSDALE",
      "HUDS", "HUDSON",
      "NLEB", "NEW LEBANON",
      "V-PH", "PHILMONT"
  });
}
	