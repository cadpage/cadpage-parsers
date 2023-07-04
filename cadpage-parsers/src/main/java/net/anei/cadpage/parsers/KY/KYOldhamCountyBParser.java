package net.anei.cadpage.parsers.KY;

public class KYOldhamCountyBParser extends KYStatePoliceCParser {

  public KYOldhamCountyBParser() {
    super("OLDHAM COUNTY");
  }

  @Override
  public String getLocName() {
    return "Oldham County, KY";
  }
}
