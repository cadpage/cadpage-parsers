package net.anei.cadpage.parsers.KY;

public class KYBrackenCountyAParser extends KYStatePoliceAParser {
  
  public KYBrackenCountyAParser() {
    super("BRACKEN COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Bracken County, KY";
  }
}
