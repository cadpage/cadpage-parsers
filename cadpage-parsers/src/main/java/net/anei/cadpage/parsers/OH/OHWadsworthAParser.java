package net.anei.cadpage.parsers.OH;


public class OHWadsworthAParser extends OHSummitCountyAParser {
  
  public OHWadsworthAParser() {
    super("WADSWORTH", "OH");
  }
  
  @Override
  public String getFilter() {
    return "info@sundance-sys.com";
  }
}
