package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Cooke County, TX
 */
public class TXCookeCountyParser extends GroupBestParser {

  public TXCookeCountyParser() {
    super(new TXCookeCountyBParser(),
          new TXCookeCountyCParser());
  }
}
