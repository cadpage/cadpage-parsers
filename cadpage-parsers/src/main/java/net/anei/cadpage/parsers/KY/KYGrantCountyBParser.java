package net.anei.cadpage.parsers.KY;

public class KYGrantCountyBParser extends KYStatePoliceBParser {

  public KYGrantCountyBParser() {
    super("GRANT COUNTY");
  }

  @Override
  public String getLocName() {
    return "Grant County, KY";
  }
}
