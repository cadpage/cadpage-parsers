package net.anei.cadpage.parsers.TX;

public class TXTexasCityAParser extends TXLaPorteAParser {

  public TXTexasCityAParser() {
    super("TEXAS CITY", "TX");
  }

  @Override
  public String getLocName() {
    return "Texas City, TX";
  }

  @Override
  public String getFilter() {
    return "CAD@texas-city-tx.org,CAD@texascitytx.gov";
  }
}
