package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Allegheny County, PA
 */


public class PAAlleghenyCountyParser extends GroupBestParser {
  
  public PAAlleghenyCountyParser() {
    super(new PAAlleghenyCountyAParser(), new PAAlleghenyCountyBParser());
  }
}
