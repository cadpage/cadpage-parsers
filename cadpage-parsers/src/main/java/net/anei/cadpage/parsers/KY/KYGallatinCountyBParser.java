package net.anei.cadpage.parsers.KY;

public class KYGallatinCountyBParser extends KYStatePoliceParser {
  
  public KYGallatinCountyBParser() {
    super("GALLATIN COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Gallatin County, KY";
  }
}
