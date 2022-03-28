package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Marion County, OR
 */

public class ORMarionCountyParser extends GroupBestParser {
  public ORMarionCountyParser() {
    super(new ORMarionCountyAParser(), new ORMarionCountyBParser());
  }
}
