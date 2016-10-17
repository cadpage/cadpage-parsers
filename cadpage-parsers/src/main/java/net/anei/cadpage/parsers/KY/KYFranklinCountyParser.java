package net.anei.cadpage.parsers.KY;

public class KYFranklinCountyParser extends KYStatePoliceParser {
  
  public KYFranklinCountyParser() {
    super("FRANKLIN COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Franklin County, KY";
  }
}
