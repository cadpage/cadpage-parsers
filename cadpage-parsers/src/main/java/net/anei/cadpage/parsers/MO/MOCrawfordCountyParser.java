package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOCrawfordCountyParser extends GroupBestParser {

  public MOCrawfordCountyParser() {
    super(new MOCrawfordCountyAParser(), new MOCrawfordCountyBParser());
  }
}
