package net.anei.cadpage.parsers.KY;

public class KYGrantCountyParser extends KYStatePoliceParser {
  
  public KYGrantCountyParser() {
    super("GRANT COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Grant County, KY";
  }
}
