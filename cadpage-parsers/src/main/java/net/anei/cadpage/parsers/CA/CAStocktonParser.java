package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Stockton, CA
 */
public class CAStocktonParser extends GroupBestParser {
  public CAStocktonParser() {
    super(new CAStocktonAParser(), new CAStocktonBParser());
  }
}
