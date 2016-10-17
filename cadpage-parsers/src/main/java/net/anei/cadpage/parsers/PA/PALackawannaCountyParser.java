package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Lackawanna County, PA
 */


public class PALackawannaCountyParser extends GroupBestParser {
  
  public PALackawannaCountyParser() {
    super(new PALackawannaCountyAParser(),
           new PALackawannaCountyAmbulanceParser());
  }
}
