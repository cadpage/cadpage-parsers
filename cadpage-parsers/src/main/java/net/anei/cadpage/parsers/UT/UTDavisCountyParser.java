package net.anei.cadpage.parsers.UT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Davis County, UT
 */
public class UTDavisCountyParser extends GroupBestParser {
  
  public UTDavisCountyParser() {
    super(new UTDavisCountyAParser(), new UTDavisCountyBParser(), 
          new UTDavisCountyCParser(), new UTDavisCountyDParser(),
          new UTDavisCountyEParser());
  }
}
