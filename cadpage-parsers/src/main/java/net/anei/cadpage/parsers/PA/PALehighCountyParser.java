package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Lehigh County, PA
 */


public class PALehighCountyParser extends GroupBestParser {
  
  public PALehighCountyParser() {
    super(new PALehighCountyAParser(),
           new PALehighCountyBParser());
  }
}
