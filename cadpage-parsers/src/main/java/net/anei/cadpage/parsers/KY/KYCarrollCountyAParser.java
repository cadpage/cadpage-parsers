package net.anei.cadpage.parsers.KY;

public class KYCarrollCountyAParser extends KYStatePoliceAParser {
  
  public KYCarrollCountyAParser() {
    super("CARROLL COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Carroll County, KY";
  }
}
