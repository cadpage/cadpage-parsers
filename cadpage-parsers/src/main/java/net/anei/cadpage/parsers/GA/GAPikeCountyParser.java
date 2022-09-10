package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Pike County, GA
 */
public class GAPikeCountyParser extends GroupBestParser {
  
  public GAPikeCountyParser() {
    super(new GAPikeCountyAParser(), new GAPikeCountyBParser());
  }
}
