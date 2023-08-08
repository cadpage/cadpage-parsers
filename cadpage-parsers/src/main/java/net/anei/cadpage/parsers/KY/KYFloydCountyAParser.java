package net.anei.cadpage.parsers.KY;

public class KYFloydCountyAParser extends KYStatePoliceCParser {

  public KYFloydCountyAParser() {
    super("FLOYD COUNTY");
  }

  @Override
  public String getLocName() {
    return "Floyd County, KY";
  }
}
