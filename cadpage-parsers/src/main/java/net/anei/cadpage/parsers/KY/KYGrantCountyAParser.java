package net.anei.cadpage.parsers.KY;

public class KYGrantCountyAParser extends KYStatePoliceCParser {

  public KYGrantCountyAParser() {
    super("GRANT COUNTY");
  }

  @Override
  public String getLocName() {
    return "Grant County, KY";
  }
}
