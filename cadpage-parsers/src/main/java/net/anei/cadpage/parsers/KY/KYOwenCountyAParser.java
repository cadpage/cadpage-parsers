package net.anei.cadpage.parsers.KY;

public class KYOwenCountyAParser extends KYStatePoliceAParser {
  
  public KYOwenCountyAParser() {
    super("OWEN COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Owen County, KY";
  }
}
