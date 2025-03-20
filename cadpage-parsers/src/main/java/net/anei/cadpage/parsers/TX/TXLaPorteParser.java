package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Leon County, TX
 */
public class TXLaPorteParser extends GroupBestParser {

  public TXLaPorteParser() {
    super(new TXLaPorteAParser(), new TXHarrisCountyGParser());
  }
}
