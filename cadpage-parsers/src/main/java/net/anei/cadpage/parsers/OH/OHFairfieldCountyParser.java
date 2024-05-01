package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Fairfield County, OH
 */

public class OHFairfieldCountyParser extends GroupBestParser {

  public OHFairfieldCountyParser() {
    super(new OHFairfieldCountyAParser(), new OHFairfieldCountyBParser());
  }
}
