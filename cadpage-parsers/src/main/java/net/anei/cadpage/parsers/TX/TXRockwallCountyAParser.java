package net.anei.cadpage.parsers.TX;

/**
 * Rockwall County, TX
 */
public class TXRockwallCountyAParser extends TXCollinCountyAParser {

  public TXRockwallCountyAParser() {
    super("ROCKWALL COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "ics.gateway@wylietexas.gov";
  }
}
