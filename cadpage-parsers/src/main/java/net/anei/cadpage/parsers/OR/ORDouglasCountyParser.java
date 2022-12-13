package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Douglas County, OR
 */

public class ORDouglasCountyParser extends GroupBestParser {
  public ORDouglasCountyParser() {
    super(new ORDouglasCountyAParser(), new ORDouglasCountyBParser(), new ORDouglasCountyCParser());
  }
}
