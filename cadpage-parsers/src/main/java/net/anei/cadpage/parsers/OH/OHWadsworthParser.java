package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Wayne County, OH
 */

public class OHWadsworthParser extends GroupBestParser {
  
  public OHWadsworthParser() {
    super(new OHWadsworthAParser(),
           new OHWadsworthBParser());
  }
}
