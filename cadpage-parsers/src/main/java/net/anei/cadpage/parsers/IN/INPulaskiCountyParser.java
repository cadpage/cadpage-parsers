package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INPulaskiCountyParser extends DispatchSPKParser {
  
  public INPulaskiCountyParser() {
    super("PULASKI COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "PulaskiCountyCAD@ipsc.in.gov,noreply@public-safety-cloud.com";
  }
}
