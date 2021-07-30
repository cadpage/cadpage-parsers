package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Lane County, OR
 */

public class ORLaneCountyParser extends GroupBestParser {
  public ORLaneCountyParser() {
    super(new ORLaneCountyAParser(), new ORLaneCountyBParser(),
          new ORLaneCountyCParser(), new ORLaneCountyDParser());
  }
}
