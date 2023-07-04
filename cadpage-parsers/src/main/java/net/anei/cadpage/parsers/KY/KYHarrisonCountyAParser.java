package net.anei.cadpage.parsers.KY;

public class KYHarrisonCountyAParser extends KYStatePoliceCParser {

  public KYHarrisonCountyAParser() {
    super("HARRISON COUNTY");
  }

  @Override
  public String getLocName() {
    return "Harrison County, KY";
  }
}
