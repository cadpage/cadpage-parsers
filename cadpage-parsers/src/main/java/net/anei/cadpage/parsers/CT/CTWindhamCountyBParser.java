package net.anei.cadpage.parsers.CT;


public class CTWindhamCountyBParser extends CTNewHavenCountyBParser {
  
  public CTWindhamCountyBParser() {
    super("WINDHAM COUNTY", "CT");
  }
  
  @Override
  public String getFilter() {
    return "wpdpaging@gmail.com";
  }
}
