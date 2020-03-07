package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Deschutes County, OR
 */

public class ORDeschutesCountyParser extends GroupBestParser {
  public ORDeschutesCountyParser() {
    super(new ORDeschutesCountyAParser(), new ORDeschutesCountyBParser());
  }
}
