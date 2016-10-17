package net.anei.cadpage.parsers.KY;

public class KYBourbonCountyParser extends KYStatePoliceParser {
  
  public KYBourbonCountyParser() {
    super("BOURBON COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Bourbon County, KY";
  }
}
