package net.anei.cadpage.parsers.TX;

/**
 * Walker County, TX
 */
public class TXWalkerCountyParser extends TXCollinCountyCParser {

  public TXWalkerCountyParser() {
    super("WALKER COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "InfoRad@co.walker.tx.us";
  }
}
