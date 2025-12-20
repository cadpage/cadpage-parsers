package net.anei.cadpage.parsers.IN;


import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Wabash County, IN
 */
public class INWabashCountyParser extends DispatchSPKParser {

  public INWabashCountyParser() {
    super("WABASH COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "noreply@public-safety-cloud.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
