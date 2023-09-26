package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * McLennan County, TX
 */
public class TXMcLennanCountyParser extends GroupBestParser {

  public TXMcLennanCountyParser() {
    super(new TXMcLennanCountyAParser(),
          new TXMcLennanCountyBParser(),
          new TXMcLennanCountyDParser(),
          new TXMcLennanCountyFParser());
  }
}
