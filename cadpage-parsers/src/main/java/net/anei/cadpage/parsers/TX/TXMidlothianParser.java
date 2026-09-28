package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Midlothian, TX
 */
public class TXMidlothianParser extends GroupBestParser {

  public TXMidlothianParser() {
    super(new TXMidlothianAParser(), new TXMidlothianBParser());
  }
}
