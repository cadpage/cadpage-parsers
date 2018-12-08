package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Clearfield County, PA
 */


public class PAClearfieldCountyParser extends GroupBestParser {
  
  public PAClearfieldCountyParser() {
    super(new PAClearfieldCountyAParser(), new PAClearfieldCountyBParser());
  }
}
