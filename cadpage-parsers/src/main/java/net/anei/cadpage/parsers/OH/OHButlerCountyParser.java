package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Butler County, OH
 */

public class OHButlerCountyParser extends GroupBestParser {
  
  public OHButlerCountyParser() {
    super(new OHButlerCountyAParser(), new OHButlerCountyBParser());
  }
}
