package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Clark County, OH
 */

public class OHClarkCountyParser extends GroupBestParser {
  
  public OHClarkCountyParser() {
    super(new OHClarkCountyAParser(), new OHClarkCountyBParser(), new OHClarkCountyCParser());
  }
}
