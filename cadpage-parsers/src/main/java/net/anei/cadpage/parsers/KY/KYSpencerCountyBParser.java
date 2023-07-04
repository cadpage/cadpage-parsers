package net.anei.cadpage.parsers.KY;

public class KYSpencerCountyBParser extends KYStatePoliceCParser {

  public KYSpencerCountyBParser() {
    super("SPENCER COUNTY");
  }

  @Override
  public String getLocName() {
    return "Spencer County, KY";
  }
}
