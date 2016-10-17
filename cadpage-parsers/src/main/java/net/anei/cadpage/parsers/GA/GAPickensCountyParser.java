package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAPickensCountyParser extends DispatchSPKParser {

  public GAPickensCountyParser() {
    this("PICKENS COUNTY", "GA");
  }
  
  protected GAPickensCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getAliasCode() {
    return "GAPickensCounty";
  }
  
  @Override
  public String getFilter() {
    return "pickens911cad@gmail.com";
  }
}
