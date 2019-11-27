package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PAWestmorelandCountyParser extends GroupBestParser {
  
  public PAWestmorelandCountyParser() {
    super(new PAWestmorelandCountyAParser(), 
          new PAWestmorelandCountyBParser());
  }
  
}