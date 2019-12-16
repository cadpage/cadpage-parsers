package net.anei.cadpage.parsers.KY;

public class KYHenryCountyAParser extends KYStatePoliceAParser {
  
  public KYHenryCountyAParser() {
    super("HENRY COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Henry County, KY";
  }
}
