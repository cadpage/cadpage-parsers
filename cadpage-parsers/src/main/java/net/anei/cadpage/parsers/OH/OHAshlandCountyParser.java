package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Ashland County, OH
 */

public class OHAshlandCountyParser extends GroupBestParser {
  
  public OHAshlandCountyParser() {
    super(new OHAshlandCountyAParser(), new OHAshlandCountyBParser());
  }
}
