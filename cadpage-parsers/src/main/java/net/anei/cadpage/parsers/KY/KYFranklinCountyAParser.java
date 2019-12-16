package net.anei.cadpage.parsers.KY;

public class KYFranklinCountyAParser extends KYStatePoliceAParser {
  
  public KYFranklinCountyAParser() {
    super("FRANKLIN COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Franklin County, KY";
  }
}
