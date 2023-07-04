package net.anei.cadpage.parsers.KY;

public class KYFranklinCountyAParser extends KYStatePoliceCParser {

  public KYFranklinCountyAParser() {
    super("FRANKLIN COUNTY");
  }

  @Override
  public String getLocName() {
    return "Franklin County, KY";
  }
}
