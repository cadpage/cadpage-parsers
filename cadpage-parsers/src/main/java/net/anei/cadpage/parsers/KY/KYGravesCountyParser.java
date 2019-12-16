package net.anei.cadpage.parsers.KY;

public class KYGravesCountyParser extends KYStatePoliceAParser {
  
  public KYGravesCountyParser() {
    super("GRAVES COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Graves County, KY";
  }
}
