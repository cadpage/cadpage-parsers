package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Columbia County, PA
 */


public class PAColumbiaCountyParser extends GroupBestParser {
  
  public PAColumbiaCountyParser() {
    super(new PAColumbiaCountyAParser(),
           new PAColumbiaCountyBParser());
  }
}
