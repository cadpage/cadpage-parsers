package net.anei.cadpage.parsers.CT;


public class CTLitchfieldCountyCParser extends CTNewHavenCountyBParser {
  
  public CTLitchfieldCountyCParser() {
    super("LITCHFIELD COUNTY", "CT");
  }
  
  @Override
  public String getFilter() {
    return "page@waterdownctpd.org";
  }
}
