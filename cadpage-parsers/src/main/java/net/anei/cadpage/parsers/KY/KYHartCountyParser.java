package net.anei.cadpage.parsers.KY;

public class KYHartCountyParser extends KYStatePoliceAParser {
  
  public KYHartCountyParser() {
    super("HART COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Hart County, KY";
  }
}
