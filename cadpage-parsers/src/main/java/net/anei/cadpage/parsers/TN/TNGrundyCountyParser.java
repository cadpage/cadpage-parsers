package net.anei.cadpage.parsers.TN;

public class TNGrundyCountyParser extends TNFranklinCountyParser {
  
  public TNGrundyCountyParser() {
    super("GRUNDY COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "Dispatch@GrundyTN911.info,dispatch@grundytn911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
