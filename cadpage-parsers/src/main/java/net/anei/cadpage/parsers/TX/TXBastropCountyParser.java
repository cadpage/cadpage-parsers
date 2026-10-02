package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Bastrop County, TX
 */
public class TXBastropCountyParser extends GroupBestParser {

  public TXBastropCountyParser() {
    super(new TXBastropCountyAParser(), new TXBastropCountyBParser());
  }
}
