package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Pickaway County, OH
 */

public class OHPickawayCountyParser extends GroupBestParser {

  public OHPickawayCountyParser() {
    super(new OHPickawayCountyAParser(), new OHPickawayCountyBParser());
  }
}
