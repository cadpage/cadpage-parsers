package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Northumberland County, PA
 */


public class PANorthumberlandCountyParser extends GroupBestParser {
  
  public PANorthumberlandCountyParser() {
    super(new PANorthumberlandCountyAParser(),
           new PANorthumberlandCountyBParser());
  }
}
