package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Cowlitz County, WA
 */
public class WACowlitzCountyParser extends GroupBestParser {

  public WACowlitzCountyParser() {
    super(new WACowlitzCountyAParser(), new WACowlitzCountyBParser());
  }
}
