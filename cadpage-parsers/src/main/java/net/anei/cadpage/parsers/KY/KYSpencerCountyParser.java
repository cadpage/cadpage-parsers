package net.anei.cadpage.parsers.KY;

public class KYSpencerCountyParser extends KYStatePoliceParser {
  
  public KYSpencerCountyParser() {
    super("SPENCER COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Spencer County, KY";
  }
}
