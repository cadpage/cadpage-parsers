package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Umatilla County, OR
 */

public class ORUmatillaCountyParser extends GroupBestParser {
  public ORUmatillaCountyParser() {
    super(new ORUmatillaCountyAParser(), new ORUmatillaCountyBParser());
  }
}
