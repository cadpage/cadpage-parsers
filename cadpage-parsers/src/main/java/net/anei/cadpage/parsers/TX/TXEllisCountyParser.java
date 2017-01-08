package net.anei.cadpage.parsers.TX;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXEllisCountyParser extends DispatchA55Parser {
  
  public TXEllisCountyParser() {
    super("ELLIS COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
}
