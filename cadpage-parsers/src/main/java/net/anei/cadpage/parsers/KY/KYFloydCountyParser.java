package net.anei.cadpage.parsers.KY;

public class KYFloydCountyParser extends KYStatePoliceParser {
  
  public KYFloydCountyParser() {
    super("FLOYD COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Floyd County, KY";
  }
}
