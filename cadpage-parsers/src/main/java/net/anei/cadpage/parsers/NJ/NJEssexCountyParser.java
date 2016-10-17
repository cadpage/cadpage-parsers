package net.anei.cadpage.parsers.NJ;

public class NJEssexCountyParser extends NJSussexCountyAParser {
  
  public NJEssexCountyParser() {
    super("ESSEX COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "@NCPD.local,@northcaldwell.org";
  }
}
