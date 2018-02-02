package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.GroupBestParser;


public class MTFlatheadCountyParser extends GroupBestParser {

  public MTFlatheadCountyParser() {
    super(new MTFlatheadCountyAParser(),
          new MTFlatheadCountyBParser(),
          new MTFlatheadCountyCParser());
  }
}
