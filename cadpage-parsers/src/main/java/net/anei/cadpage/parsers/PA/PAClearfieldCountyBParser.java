package net.anei.cadpage.parsers.PA;

/**
 * Clearfield County, PA (B)
 */
public class PAClearfieldCountyBParser extends PAElkCountyParser {
  
  public PAClearfieldCountyBParser() {
    super("CLEARFIELD COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "alerts@clearfieldalerts.com";
  }
}
