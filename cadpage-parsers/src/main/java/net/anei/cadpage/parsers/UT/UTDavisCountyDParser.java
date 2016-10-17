package net.anei.cadpage.parsers.UT;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class UTDavisCountyDParser extends DispatchA55Parser {
  
  public UTDavisCountyDParser() {
    super("DAVIS COUNTY", "UT");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
}
