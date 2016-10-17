package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.GroupBestParser;


public class MTLewisAndClarkCountyParser extends GroupBestParser {

  public MTLewisAndClarkCountyParser() {
    super(new MTLewisAndClarkCountyAParser(),
           new MTLewisAndClarkCountyBParser());
  }
}
