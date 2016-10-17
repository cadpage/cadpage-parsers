package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Lorain County, OH
 */

public class OHLorainCountyParser extends GroupBestParser {
  
  public OHLorainCountyParser() {
    super(new OHLorainCountyAParser(), new OHLorainCountyBParser());
  }
}
