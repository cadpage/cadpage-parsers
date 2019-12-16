package net.anei.cadpage.parsers.KY;

public class KYHenryCountyBParser extends KYStatePoliceBParser {
  
  public KYHenryCountyBParser() {
    super("HENRY COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Henry County, KY";
  }
}
