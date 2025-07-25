package net.anei.cadpage.parsers.TX;

/**
 * Flower Mound, TX
 */
public class TXFlowerMoundParser extends TXCollinCountyAParser {

  public TXFlowerMoundParser() {
    super("DENTON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "CADPaging-NOREPLY@flower-mound.com,notifications@athenaics.com";
  }
}
