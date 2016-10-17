package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Humble, TX
 */
public class TXHumbleParser extends GroupBestParser {
  
  public TXHumbleParser() {
    super(new TXHumbleAParser(), new TXHumbleBParser());
  }
}
