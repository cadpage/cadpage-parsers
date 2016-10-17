package net.anei.cadpage.parsers.KY;

public class KYScottCountyAParser extends KYStatePoliceParser {
  
  public KYScottCountyAParser() {
    super("SCOTT COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Scott County, KY";
  }
}
