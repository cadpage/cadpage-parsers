package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Hood River County, OR
 */

public class ORHoodRiverCountyParser extends GroupBestParser {
  public ORHoodRiverCountyParser() {
    super(new ORHoodRiverCountyAParser(), new ORHoodRiverCountyBParser());
  }
}
