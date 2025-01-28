package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Hays County, TX
 */
public class TXHaysCountyParser extends GroupBestParser {

  public TXHaysCountyParser() {
    super(new TXHaysCountyBParser(),
          new TXHaysCountyDParser());
  }
}
