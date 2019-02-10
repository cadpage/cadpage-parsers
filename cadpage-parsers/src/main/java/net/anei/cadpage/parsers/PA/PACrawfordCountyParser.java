package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Crawford County, PA
*/


public class PACrawfordCountyParser extends GroupBestParser {
  
  public PACrawfordCountyParser() {
    super(new PACrawfordCountyAParser(),
          new PACrawfordCountyBParser());
  }
}
