package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYOswegoCountyParser extends GroupBestParser {
  
  public NYOswegoCountyParser() {
    super(new NYOswegoCountyAParser(),
           new NYOswegoCountyBParser());
  }
}
