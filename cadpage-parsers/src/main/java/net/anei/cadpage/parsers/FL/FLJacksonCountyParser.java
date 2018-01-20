package net.anei.cadpage.parsers.FL;

public class FLJacksonCountyParser extends FLBayCountyParser {
  
  public FLJacksonCountyParser() {
    super("HOLMES COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "jcsodispatch@bayso.org";
  }
}
