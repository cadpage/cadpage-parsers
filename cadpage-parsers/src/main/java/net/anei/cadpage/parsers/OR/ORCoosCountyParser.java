package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Coos County, OR
 */

public class ORCoosCountyParser extends GroupBestParser {
  public ORCoosCountyParser() {
    super(new ORCoosCountyAParser(), new ORCoosCountyBParser());
  }
}
