package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Seabrook, TX
 */

public class TXSeabrookParser extends GroupBestParser {

  public TXSeabrookParser() {
    super(new TXSeabrookAParser(), new TXHarrisCountyGParser());
  }


  @Override
  public String getLocName() {
    return "Seabrook, TX";
  }
}
