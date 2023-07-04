package net.anei.cadpage.parsers.KY;

public class KYShelbyCountyParser extends KYStatePoliceCParser {

  public KYShelbyCountyParser() {
    super("SHELBY COUNTY");
  }

  @Override
  public String getLocName() {
    return "Shelby County, KY";
  }
}
