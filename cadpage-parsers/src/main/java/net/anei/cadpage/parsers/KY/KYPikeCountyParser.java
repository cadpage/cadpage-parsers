package net.anei.cadpage.parsers.KY;

public class KYPikeCountyParser extends KYStatePoliceCParser {

  public KYPikeCountyParser() {
    super("PIKE COUNTY");
  }

  @Override
  public String getLocName() {
    return "Pike County, KY";
  }
}
