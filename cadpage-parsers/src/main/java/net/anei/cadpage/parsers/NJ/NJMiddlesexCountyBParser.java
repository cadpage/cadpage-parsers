package net.anei.cadpage.parsers.NJ;

public class NJMiddlesexCountyBParser extends NJSussexCountyAParser {
  
  public NJMiddlesexCountyBParser() {
    super("MIDDLESEX COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "@SBPOLICE.COM";
  }
}
