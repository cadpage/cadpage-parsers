package net.anei.cadpage.parsers.CT;


public class CTFairfieldCountyCParser extends CTNewHavenCountyBParser {
  
  public CTFairfieldCountyCParser() {
    super(CTFairfieldCountyParser.CITY_LIST, "FAIRFIELD COUNTY", "CT");
  }
  
  @Override
  public String getFilter() {
    return "paging@sheltonpolice.net";
  }
}
