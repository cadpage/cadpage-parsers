package net.anei.cadpage.parsers.KY;

public class KYHartCountyParser extends KYStatePoliceParser {
  
  public KYHartCountyParser() {
    super("HART COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Hart County, KY";
  }
}
