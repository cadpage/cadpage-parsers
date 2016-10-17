package net.anei.cadpage.parsers.KY;

public class KYShelbyCountyParser extends KYStatePoliceParser {
  
  public KYShelbyCountyParser() {
    super("SHELBY COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Shelby County, KY";
  }
}
