package net.anei.cadpage.parsers.TN;

public class TNBledsoeCountyParser extends TNRheaCountyBParser {

  public TNBledsoeCountyParser() {
    super("BLEDSOE COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@BledsoeTNe911.info";
  }
}
