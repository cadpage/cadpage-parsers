package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Carroll County, OH
 */

public class OHCarrollCountyParser extends GroupBestParser {
  
  public OHCarrollCountyParser() {
    super(new OHCarrollCountyAParser(), new OHCarrollCountyBParser());
  }
}
