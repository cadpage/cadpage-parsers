package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


/*
Madison County, NY

*/


public class NYMadisonCountyParser extends GroupBestParser {
  
  public NYMadisonCountyParser() {
    super(new NYMadisonCountyAParser(), 
          new NYMadisonCountyBParser(),
          new NYMadisonCountyCParser(),
          new GroupBlockParser(),
          new NYMadisonCountyGLASParser());
  }
}
	