package net.anei.cadpage.parsers.KY;

public class KYHenryCountyParser extends KYStatePoliceParser {
  
  public KYHenryCountyParser() {
    super("HENRY COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Henry County, KY";
  }
}
