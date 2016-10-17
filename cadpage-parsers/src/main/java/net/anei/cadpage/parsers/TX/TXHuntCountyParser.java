package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Hunt County, TX
 */
public class TXHuntCountyParser extends GroupBestParser {
  
  public TXHuntCountyParser() {
    super(new TXHuntCountyAParser(), new TXHuntCountyBParser());
  }
}
