package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Van Zandt County, TX
 */

public class TXVanZandtCountyParser extends GroupBestParser {

  public TXVanZandtCountyParser() {
    super(new TXVanZandtCountyAParser(), new TXVanZandtCountyBParser());
  }

}
