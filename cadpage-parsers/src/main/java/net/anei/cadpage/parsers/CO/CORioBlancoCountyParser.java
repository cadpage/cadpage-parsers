package net.anei.cadpage.parsers.CO;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class CORioBlancoCountyParser extends DispatchA55Parser {
  
  public CORioBlancoCountyParser() {
    super("RIO BLANCO COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
  
}
