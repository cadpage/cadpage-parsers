package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/**
* Linn County, OR
 */

public class ORLinnCountyParser extends GroupBestParser {
  public ORLinnCountyParser() {
    super(new ORLinnCountyAParser(), new ORLinnCountyBParser(), new GroupBlockParser(), new ORLinnCountyCParser());
  }
}
