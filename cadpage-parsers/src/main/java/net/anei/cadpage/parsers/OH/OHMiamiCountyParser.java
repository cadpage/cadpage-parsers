package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Miami County, OH
 */

public class OHMiamiCountyParser extends GroupBestParser {
  
  public OHMiamiCountyParser() {
    super(new OHMiamiCountyAParser(), new OHMiamiCountyBParser());
  }
}
