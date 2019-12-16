package net.anei.cadpage.parsers.KY;

public class KYScottCountyAParser extends KYStatePoliceAParser {
  
  public KYScottCountyAParser() {
    super("SCOTT COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Scott County, KY";
  }
}
