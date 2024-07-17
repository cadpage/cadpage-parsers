package net.anei.cadpage.parsers.NJ;

public class NJEssexCountyAParser extends NJSussexCountyAParser {
  
  public NJEssexCountyAParser() {
    super("ESSEX COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "@northcaldwell.org,mail@westcaldwellpd.org";
  }
}
