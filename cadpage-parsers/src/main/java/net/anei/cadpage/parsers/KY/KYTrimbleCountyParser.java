package net.anei.cadpage.parsers.KY;

public class KYTrimbleCountyParser extends KYStatePoliceParser {
  
  public KYTrimbleCountyParser() {
    super("TRIMBLE COUNTY");
  }
  
  @Override
  public String getLocName() {
    return "Trimble County, KY";
  }
}
