package net.anei.cadpage.parsers.KY;

public class KYLyonCountyParser extends KYStatePoliceCParser {

  public KYLyonCountyParser() {
    super("LYON COUNTY");
  }

  @Override
  public String getLocName() {
    return "Lyon County, KY";
  }
}
