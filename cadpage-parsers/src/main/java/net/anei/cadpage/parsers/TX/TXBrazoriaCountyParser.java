package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Brazoria County, TX
 */
public class TXBrazoriaCountyParser extends GroupBestParser {

  public TXBrazoriaCountyParser() {
    super(new TXBrazoriaCountyAParser(), new TXBrazoriaCountyBParser(),
          new TXBrazoriaCountyCParser());
  }
}
