package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Jefferson County, OH
 */

public class OHJeffersonCountyParser extends GroupBestParser {
  
  public OHJeffersonCountyParser() {
    super(new OHJeffersonCountyAParser(), new OHJeffersonCountyBParser());
  }
}
