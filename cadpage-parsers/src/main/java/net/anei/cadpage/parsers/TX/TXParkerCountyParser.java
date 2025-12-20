package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Parker County, TX
 */
public class TXParkerCountyParser extends GroupBestParser {

  public TXParkerCountyParser() {
    super(new TXParkerCountyAParser(),
          new TXParkerCountyBParser(),
          new TXParkerCountyDParser());
  }
}
