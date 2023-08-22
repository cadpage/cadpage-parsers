package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Denton County, TX
 */
public class TXDentonCountyParser extends GroupBestParser {

  public TXDentonCountyParser() {
    super(new TXDentonCountyAParser(),
          new TXDentonCountyBParser(),
          new TXDentonCountyCParser(),
          new TXDentonCountyEParser());
  }
}