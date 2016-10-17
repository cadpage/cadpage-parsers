package net.anei.cadpage.parsers.KY;

public class KYBooneCountyBParser extends KYStatePoliceParser {
  
  public KYBooneCountyBParser() {
    super("BOONE COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Boone County, KY";
  }
}
