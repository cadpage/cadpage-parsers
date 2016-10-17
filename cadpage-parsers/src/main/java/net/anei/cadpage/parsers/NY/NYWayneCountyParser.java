package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYWayneCountyParser extends GroupBestParser {
  
  public NYWayneCountyParser() {
    super(new NYWayneCountyAParser(),
           new NYWayneCountyBParser());
  }
}
