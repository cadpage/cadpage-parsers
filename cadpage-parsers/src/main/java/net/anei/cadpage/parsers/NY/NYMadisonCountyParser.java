package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Madison County, NY

*/


public class NYMadisonCountyParser extends GroupBestParser {
  
  public NYMadisonCountyParser() {
    super(new NYMadisonCountyAParser(), 
          new NYMadisonCountyBParser(),
          new NYMadisonCountyGLASParser());
  }
}
	