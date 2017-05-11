package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Marshall County, NC
 */
public class KYMarshallCountyAParser extends DispatchGeoconxParser {
  
  public KYMarshallCountyAParser() {
    super("MARSHALL COUNTY", "KY", GCX_FLG_EMPTY_SUBJECT_OK);
  }
  
  @Override
  public String getFilter() {
    return "911@mcfire.us";
  }
}
