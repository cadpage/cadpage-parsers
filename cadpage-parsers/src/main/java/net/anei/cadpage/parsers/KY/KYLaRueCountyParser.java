package net.anei.cadpage.parsers.KY;

public class KYLaRueCountyParser extends KYStatePoliceBParser {

  public KYLaRueCountyParser() {
    super("LARUE COUNTY");
  }

  @Override
  public String getFilter() {
    return append("noreply@public-safety-cloud.com", ",", super.getFilter());
  }
}
