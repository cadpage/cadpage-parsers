package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Marshall County, AL
 */
public class ALMarshallCountyBParser extends DispatchGeoconxParser {


  public ALMarshallCountyBParser() {
    super("MARSHALL COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "@911email.org,@911email.net";
  }
}
