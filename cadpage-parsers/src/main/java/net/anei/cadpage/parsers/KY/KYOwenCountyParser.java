package net.anei.cadpage.parsers.KY;

public class KYOwenCountyParser extends KYStatePoliceParser {
  
  public KYOwenCountyParser() {
    super("OWEN COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Owen County, KY";
  }
}
