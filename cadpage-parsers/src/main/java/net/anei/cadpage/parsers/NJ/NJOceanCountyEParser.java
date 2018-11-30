package net.anei.cadpage.parsers.NJ;

public class NJOceanCountyEParser extends NJSussexCountyCParser {
  
  public NJOceanCountyEParser() {
    super("OCEAN COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@lawsoftweb.onmicrosoft.com";
  }
}
