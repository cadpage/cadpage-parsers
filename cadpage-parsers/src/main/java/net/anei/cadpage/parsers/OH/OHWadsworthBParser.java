package net.anei.cadpage.parsers.OH;


public class OHWadsworthBParser extends OHSummitCountyBParser {
  
  public OHWadsworthBParser() {
    super("WADSWORTH", "OH");
  }
  
  @Override
  public String getFilter() {
    return "info@sundance-sys.com";
  }
}
