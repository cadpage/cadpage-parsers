package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Williams County, OH
 */

public class OHWilliamsCountyParser extends GroupBestParser {
  
  public OHWilliamsCountyParser() {
    super(new OHWilliamsCountyAParser(),
          new OHWilliamsCountyBParser());
  }
}
