package net.anei.cadpage.parsers.KY;

public class KYPendletonCountyAParser extends KYStatePoliceAParser {
  
  public KYPendletonCountyAParser() {
    super("PENDLETON COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Pendleton County, KY";
  }
}
