package net.anei.cadpage.parsers.TX;

public class TXTexasCityParser extends TXLaPorteParser {
  
  public TXTexasCityParser() {
    super("TEXAS CITY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "CAD@texas-city-tx.org,CAD@texascitytx.gov";
  }
}
