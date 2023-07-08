package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Sherman County, OR
 */

public class ORShermanCountyParser extends GroupBestParser {
  public ORShermanCountyParser() {
    super(new ORShermanCountyAParser(),
          new ORShermanCountyBParser(),
          new ORShermanCountyCParser());
  }
}
