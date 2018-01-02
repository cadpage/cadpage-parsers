package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Columbiana County, OH
 */

public class OHColumbianaCountyParser extends GroupBestParser {
  public OHColumbianaCountyParser() {
    super(new OHColumbianaCountyAParser(), new OHColumbianaCountyBParser());
  }
}
