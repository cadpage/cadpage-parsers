package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Auglaize County, OH
 */

public class OHAuglaizeCountyParser extends GroupBestParser {
  
  public OHAuglaizeCountyParser() {
    super(new OHAuglaizeCountyAParser(), 
          new OHAuglaizeCountyBParser(),
          new OHAuglaizeCountyCParser());
  }
}
