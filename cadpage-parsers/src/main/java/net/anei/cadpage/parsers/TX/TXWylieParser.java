package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Wylie, TX
 */

public class TXWylieParser extends GroupBestParser {

  public TXWylieParser() {
    super(new TXWylieParserA(), new TXWylieBParser());
  }

}
