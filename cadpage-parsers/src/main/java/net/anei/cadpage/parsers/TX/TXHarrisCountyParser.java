package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Harris County, TX
 */
public class TXHarrisCountyParser extends GroupBestParser {

  public TXHarrisCountyParser() {
    super(new TXHarrisCountyAParser(),
          new TXHarrisCountyBParser(),
          new TXHarrisCountyCParser());
  }
}
