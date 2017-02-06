package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Linn County, OR
 */

public class ORLinnCountyParser extends GroupBestParser {
  public ORLinnCountyParser() {
    super(new ORLinnCountyAParser(), new ORLinnCountyBParser());
  }
}
