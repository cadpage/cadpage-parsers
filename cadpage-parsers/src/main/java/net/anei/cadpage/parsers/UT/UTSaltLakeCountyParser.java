package net.anei.cadpage.parsers.UT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Salt Lake County, UT
 */
public class UTSaltLakeCountyParser extends GroupBestParser {
  
  public UTSaltLakeCountyParser() {
    super(new UTSaltLakeCountyAParser(), new UTSaltLakeCountyBParser());
  }
}
