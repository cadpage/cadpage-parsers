package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Orange County, NY
 */


public class NYOrangeCountyParser extends GroupBestParser {
  
  public NYOrangeCountyParser() {
    super(new NYOrangeCountyAParser(),
           new NYOrangeCountyBParser(),
           new NYOrangeCountyCParser(),
           new NYOrangeCountyDParser());
  }
}
