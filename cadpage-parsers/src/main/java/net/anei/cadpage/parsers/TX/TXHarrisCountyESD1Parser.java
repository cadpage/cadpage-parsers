package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

public class TXHarrisCountyESD1Parser extends GroupBestParser {

  public TXHarrisCountyESD1Parser() {
    super(new TXHarrisCountyESD1AParser(), new TXHarrisCountyESD1BParser());
  }
}
