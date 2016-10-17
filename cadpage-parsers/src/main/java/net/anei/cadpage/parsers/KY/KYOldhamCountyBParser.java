package net.anei.cadpage.parsers.KY;

public class KYOldhamCountyBParser extends KYStatePoliceParser {
  
  public KYOldhamCountyBParser() {
    super("OLDHAM COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Oldham County, KY";
  }
}
