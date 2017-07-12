package net.anei.cadpage.parsers.CT;


public class CTFairfieldCountyCParser extends CTNewHavenCountyBParser {
  
  public CTFairfieldCountyCParser() {
    super("FAIRFIELD COUNTY", "CT");
  }
  
  @Override
  public String getFilter() {
    return "paging@sheltonpolice.net,paging@townofstratford.com";
  }
}
