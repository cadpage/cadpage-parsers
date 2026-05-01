package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Union County, NJ
*/


public class NJUnionCountyParser extends GroupBestParser {

  public NJUnionCountyParser() {
    super(new NJUnionCountyAParser(), new NJUnionCountyBParser());
  }
}
