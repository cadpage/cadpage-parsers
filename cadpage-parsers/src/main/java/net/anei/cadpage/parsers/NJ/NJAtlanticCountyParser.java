package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Atlantic County, NJ

*/

public class NJAtlanticCountyParser extends GroupBestParser {

  public NJAtlanticCountyParser() {
    super(new NJAtlanticCountyAParser(),
          new NJAtlanticCountyBParser(),
          new NJAtlanticCountyCParser(),
          new NJAtlanticCountyDParser());
  }
}
