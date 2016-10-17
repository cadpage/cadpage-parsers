package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Marshall County, NC
 */
public class KYMarshallCountyParser extends DispatchGeoconxParser {
  
  public KYMarshallCountyParser() {
    super("MARSHALL COUNTY", "KY", GCX_FLG_EMPTY_SUBJECT_OK);
  }
}
