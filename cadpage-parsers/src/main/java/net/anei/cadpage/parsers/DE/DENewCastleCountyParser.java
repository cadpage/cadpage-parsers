package net.anei.cadpage.parsers.DE;

import net.anei.cadpage.parsers.GroupBestParser;


public class DENewCastleCountyParser extends GroupBestParser {

  public DENewCastleCountyParser() {
    super(new DENewCastleCountyAParser(),
           new DENewCastleCountyBParser(),
           new DENewCastleCountyDParser(),
           new DENewCastleCountyEParser(),
           new DENewCastleCountyGParser());
  }
}


