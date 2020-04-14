package net.anei.cadpage.parsers.NJ;

public class NJOceanCountyFParser extends NJSalemCountyCParser {
  
  public NJOceanCountyFParser() {
    super("OCEAN COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "cad@staffordpolice.org";
  }
}
