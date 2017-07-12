package net.anei.cadpage.parsers.CT;

public class CTHartfordCountyFarmingtonParser extends CTNewHavenCountyBParser {
  
  public CTHartfordCountyFarmingtonParser() {
    super("HARTFORD COUNTY", "CT");
  }
  
  @Override
  public String getFilter() {
    return "paging@rockyhillct.gov,pdpaging@farmington-ct.org,dispatch@westhartford.org,FirePaging@TownofCantonCT.org";
  }
}
