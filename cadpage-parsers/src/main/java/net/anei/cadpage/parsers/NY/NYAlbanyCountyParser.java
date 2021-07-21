package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Albany County, NY

*/


public class NYAlbanyCountyParser extends GroupBestParser {

  public NYAlbanyCountyParser() {
    super(new NYAlbanyCountyAParser(),
          new NYAlbanyCountyBParser(),
          new NYAlbanyCountyCParser(),
          new NYAlbanyCountyDParser(),
          new NYAlbanyCountyEParser());
  }
}
