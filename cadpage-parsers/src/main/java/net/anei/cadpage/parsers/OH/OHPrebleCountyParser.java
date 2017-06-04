package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Preble County, OH
 */

public class OHPrebleCountyParser extends GroupBestParser {
  
  public OHPrebleCountyParser() {
    super(new OHPrebleCountyAParser(), new OHPrebleCountyBParser());
  }
}
