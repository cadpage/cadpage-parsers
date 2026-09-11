package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Franklin County, OH
 */

public class OHFranklinCountyParser extends GroupBestParser {

  public OHFranklinCountyParser() {
    super(new OHFranklinCountyAParser(), new OHFranklinCountyBParser());
  }
}
