package net.anei.cadpage.parsers.KY;

public class KYTrimbleCountyAParser extends KYStatePoliceCParser {

  public KYTrimbleCountyAParser() {
    super("TRIMBLE COUNTY");
  }

  @Override
  public String getLocName() {
    return "Trimble County, KY";
  }
}
