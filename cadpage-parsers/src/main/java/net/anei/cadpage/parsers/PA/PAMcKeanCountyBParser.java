package net.anei.cadpage.parsers.PA;

/**
 * Clearfield County, PA (B)
 */
public class PAMcKeanCountyBParser extends PAElkCountyParser {
  
  public PAMcKeanCountyBParser() {
    super("MCKEAN COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "alerts@mckeancounty.ealertgov.com";
  }
}
