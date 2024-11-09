package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Muskingum County, OH
 */

public class OHMuskingumCountyParser extends GroupBestParser {

  public OHMuskingumCountyParser() {
    super(new OHMuskingumCountyAParser(),
          new OHMuskingumCountyBParser(),
          new OHMuskingumCountyCParser(),
          new OHMuskingumCountyDParser(),
          new OHMuskingumCountyEParser());
  }
}
