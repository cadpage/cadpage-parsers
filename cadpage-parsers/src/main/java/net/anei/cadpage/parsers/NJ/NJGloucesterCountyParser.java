package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Gloucester County, NJ
*/


public class NJGloucesterCountyParser extends GroupBestParser {
  
  public NJGloucesterCountyParser() {
    super(new NJGloucesterCountyAParser(), new NJGloucesterCountyBParser());
  }
}
