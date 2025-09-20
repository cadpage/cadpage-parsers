package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INCassCountyParser extends DispatchSPKParser {

  public INCassCountyParser() {
    super("CASS COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "noreply@public-safety-cloud.com,pagegate@howardcountyin.gov";
  }
}
