package net.anei.cadpage.parsers.NY;


import net.anei.cadpage.parsers.GroupBestParser;

/*
Rockland County, NY
*/

public class NYRocklandCountyParser extends GroupBestParser {
  
  public NYRocklandCountyParser() {
    super(new NYRocklandCountyAParser(), new NYRocklandCountyBParser(),
           new NYRocklandCountyCParser());
  }
}
