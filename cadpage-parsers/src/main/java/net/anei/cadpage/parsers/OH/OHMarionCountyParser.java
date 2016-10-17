package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Marion County, OH
 */

public class OHMarionCountyParser extends GroupBestParser {
  
  public OHMarionCountyParser() {
    super(new OHMarionCountyAParser(), new OHMarionCountyBParser());
  }
}
