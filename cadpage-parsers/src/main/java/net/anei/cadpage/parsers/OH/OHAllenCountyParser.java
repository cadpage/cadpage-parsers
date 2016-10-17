package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Allen County, OH
 */

public class OHAllenCountyParser extends GroupBestParser {
  
  public OHAllenCountyParser() {
    super(new OHAllenCountyAParser(), new OHAllenCountyBParser());
  }
}
