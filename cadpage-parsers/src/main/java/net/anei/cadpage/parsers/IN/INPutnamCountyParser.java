package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INPutnamCountyParser extends DispatchSPKParser {
  
  public INPutnamCountyParser() {
    super("PUTNAM COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@putnamcounty911.org";
  }

}
