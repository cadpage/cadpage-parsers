package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYMarshallCountyBParser extends DispatchSPKParser {
  
  public KYMarshallCountyBParser() {
    super("MARSHALL COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "sysadmin@marshallcountyky.gov";
  }
}
