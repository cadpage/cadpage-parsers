package net.anei.cadpage.parsers.KY;

public class KYPendletonCountyAParser extends KYStatePoliceParser {
  
  public KYPendletonCountyAParser() {
    super("PENDLETON COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Pendleton County, KY";
  }
}
