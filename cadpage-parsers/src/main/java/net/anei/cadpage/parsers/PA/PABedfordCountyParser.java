package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Bedford County, PA
 */


public class PABedfordCountyParser extends GroupBestParser {
  
  public PABedfordCountyParser() {
    super(new PABedfordCountyAParser(), new PABedfordCountyBParser());
  }
}
