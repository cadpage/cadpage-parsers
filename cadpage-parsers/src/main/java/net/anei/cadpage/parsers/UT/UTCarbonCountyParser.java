package net.anei.cadpage.parsers.UT;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class UTCarbonCountyParser extends DispatchA55Parser {
  
  public UTCarbonCountyParser() {
    super("CARBON COUNTY", "UT");
  }
  
  @Override
  public String getFilter() {
    return "priceutdispatch@gmail.com;";
  }
}
