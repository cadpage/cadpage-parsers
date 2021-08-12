package net.anei.cadpage.parsers.TX;
import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXEllisCountyAParser extends DispatchA64Parser {

  public TXEllisCountyAParser() {
    super("ELLIS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com,ereports@eforcesoftware.com";
  }
}
