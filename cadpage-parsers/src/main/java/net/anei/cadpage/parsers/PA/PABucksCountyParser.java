package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Bucks County, PA
 */


public class PABucksCountyParser extends GroupBestParser {
  
  public PABucksCountyParser() {
    super(new PABucksCountyAParser(), new PABucksCountyBParser());
  }
}
