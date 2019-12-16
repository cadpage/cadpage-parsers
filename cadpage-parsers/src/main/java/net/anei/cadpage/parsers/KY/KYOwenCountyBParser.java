package net.anei.cadpage.parsers.KY;

public class KYOwenCountyBParser extends KYStatePoliceBParser {
  
  public KYOwenCountyBParser() {
    super("OWEN COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Owen County, KY";
  }
}
