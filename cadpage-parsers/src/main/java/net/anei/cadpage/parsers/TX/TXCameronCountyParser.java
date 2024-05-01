package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Cameron County, TX
 */
public class TXCameronCountyParser extends GroupBestParser {

  public TXCameronCountyParser() {
    super(new TXCameronCountyAParser(),
          new TXCameronCountyBParser(),
          new TXCameronCountyDParser());
  }
}
