package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Hidalgo County, TX
 */
public class TXHidalgoCountyParser extends GroupBestParser {

  public TXHidalgoCountyParser() {
    super(new TXHidalgoCountyAParser(), new TXHidalgoCountyBParser());
  }
}
