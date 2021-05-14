package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Henderson County, TX
 */
public class TXHendersonCountyParser extends GroupBestParser {

  public TXHendersonCountyParser() {
    super(new TXHendersonCountyAParser(), new TXHendersonCountyBParser());
  }
}
