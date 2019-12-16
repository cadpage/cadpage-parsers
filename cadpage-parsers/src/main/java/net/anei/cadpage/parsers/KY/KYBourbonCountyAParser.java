package net.anei.cadpage.parsers.KY;

public class KYBourbonCountyAParser extends KYStatePoliceAParser {
  
  public KYBourbonCountyAParser() {
    super("BOURBON COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Bourbon County, KY";
  }
}
