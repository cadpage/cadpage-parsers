package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Northampton County, PA
 */


public class PANorthamptonCountyParser extends GroupBestParser {
  
  public PANorthamptonCountyParser() {
    super(new PANorthamptonCountyAParser(),
          new PANorthamptonCountyBParser(),
          new PANorthamptonCountyCParser());
  }
}
