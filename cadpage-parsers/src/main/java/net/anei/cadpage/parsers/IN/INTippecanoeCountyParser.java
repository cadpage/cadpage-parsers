package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INTippecanoeCountyParser extends DispatchSPKParser {
  
  public INTippecanoeCountyParser() {
    super("TIPPECANOE COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "caliber_alert@tippecanoe.in.gov";
  }

}
