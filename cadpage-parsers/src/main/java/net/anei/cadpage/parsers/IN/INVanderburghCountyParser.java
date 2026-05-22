package net.anei.cadpage.parsers.IN;


import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/*
 * Vanderburgh County, IN
 */
public class INVanderburghCountyParser extends DispatchSPKParser {

  public INVanderburghCountyParser() {
    super("VANDERBURGH COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "noreply@public-safety-cloud.com";
  }
}
