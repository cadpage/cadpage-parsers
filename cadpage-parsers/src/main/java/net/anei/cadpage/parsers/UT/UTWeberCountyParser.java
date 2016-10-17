package net.anei.cadpage.parsers.UT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Weber County, UT
 */
public class UTWeberCountyParser extends GroupBestParser {
  
  public UTWeberCountyParser() {
    super(new UTWeberCountyAParser(), new UTWeberCountyBParser());
  }
}
