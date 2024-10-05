package net.anei.cadpage.parsers.KY;

public class KYShelbyCountyAParser extends KYStatePoliceCParser {

  public KYShelbyCountyAParser() {
    super("SHELBY COUNTY");
  }

  @Override
  public String getLocName() {
    return "Shelby County, KY";
  }
}
