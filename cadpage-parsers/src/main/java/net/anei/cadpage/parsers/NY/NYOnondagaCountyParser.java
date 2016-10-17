package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Onondaga County, NY

*/


public class NYOnondagaCountyParser extends GroupBestParser {

  public NYOnondagaCountyParser() {
    super(new NYOnondagaCountyAParser(), 
           new NYOnondagaCountyBParser(),
           new NYOnondagaCountyCParser(),
           new NYOnondagaCountyDParser(),
           new NYOnondagaCountyMetroWestParser(),
           new NYOnondagaCountyEParser());
  }
}
	