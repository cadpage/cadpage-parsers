package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Lake County, OH
 */

public class OHLakeCountyParser extends GroupBestParser {
  
  public OHLakeCountyParser() {
    super(new OHLakeCountyAParser(), new OHLakeCountyBParser());
  }
}
