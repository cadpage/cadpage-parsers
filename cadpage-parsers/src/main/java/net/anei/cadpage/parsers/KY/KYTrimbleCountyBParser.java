package net.anei.cadpage.parsers.KY;

public class KYTrimbleCountyBParser extends KYStatePoliceBParser {
  
  public KYTrimbleCountyBParser() {
    super("TRIMBLE COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Trimble County, KY";
  }
}
