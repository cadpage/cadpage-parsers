package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Jefferson County, OR
 */

public class ORJeffersonCountyParser extends GroupBestParser {
  public ORJeffersonCountyParser() {
    super(new ORJeffersonCountyAParser(),
          new ORJeffersonCountyBParser(),
          new ORJeffersonCountyCParser());
  }
}
