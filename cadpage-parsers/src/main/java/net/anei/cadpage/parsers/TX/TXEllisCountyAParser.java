package net.anei.cadpage.parsers.TX;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXEllisCountyAParser extends DispatchA55Parser {

  public TXEllisCountyAParser() {
    super("ELLIS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
