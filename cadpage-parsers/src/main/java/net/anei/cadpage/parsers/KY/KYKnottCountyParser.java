package net.anei.cadpage.parsers.KY;

public class KYKnottCountyParser extends KYStatePoliceCParser {

  public KYKnottCountyParser() {
    super("KNOTT COUNTY");
  }

  @Override
  public String getLocName() {
    return "Knott County, KY";
  }
}
