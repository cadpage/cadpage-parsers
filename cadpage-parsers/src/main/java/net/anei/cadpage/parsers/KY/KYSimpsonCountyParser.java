package net.anei.cadpage.parsers.KY;

public class KYSimpsonCountyParser extends KYStatePoliceAParser {
  
  public KYSimpsonCountyParser() {
    super("SIMPSON COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Simpson County, KY";
  }
}
