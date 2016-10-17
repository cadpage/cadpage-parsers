package net.anei.cadpage.parsers.NJ;

public class NJCapeMayCountyParser extends NJSussexCountyAParser {
  
  
  public NJCapeMayCountyParser() {
    super("CAPE MAY COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "@middle";
  }
}
