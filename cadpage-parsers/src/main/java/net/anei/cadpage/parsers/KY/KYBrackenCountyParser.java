package net.anei.cadpage.parsers.KY;

public class KYBrackenCountyParser extends KYStatePoliceParser {
  
  public KYBrackenCountyParser() {
    super("BRACKEN COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Bracken County, KY";
  }
}
