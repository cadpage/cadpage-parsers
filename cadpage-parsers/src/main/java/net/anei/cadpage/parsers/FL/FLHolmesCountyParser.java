package net.anei.cadpage.parsers.FL;

public class FLHolmesCountyParser extends FLBayCountyParser {
  
  public FLHolmesCountyParser() {
    super("HOLMES COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "PAGING@HOLMESCOSHERIFF.ORG,paging@holmesso.org";
  }
}
