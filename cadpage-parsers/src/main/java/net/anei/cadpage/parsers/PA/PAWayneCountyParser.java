package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PAWayneCountyParser extends GroupBestParser {
  
  public PAWayneCountyParser() {
    super(new PAWayneCountyAParser(), 
          new PAWayneCountyBParser());
  }
  
}