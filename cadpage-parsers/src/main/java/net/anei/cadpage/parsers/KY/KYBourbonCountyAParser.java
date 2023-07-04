package net.anei.cadpage.parsers.KY;

public class KYBourbonCountyAParser extends KYStatePoliceCParser {

  public KYBourbonCountyAParser() {
    super("BOURBON COUNTY");
  }

  @Override
  public String getLocName() {
    return "Bourbon County, KY";
  }
}
