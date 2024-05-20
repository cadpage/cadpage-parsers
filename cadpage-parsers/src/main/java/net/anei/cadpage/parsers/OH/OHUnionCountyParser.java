package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Union County, OH
 */

public class OHUnionCountyParser extends GroupBestParser {

  public OHUnionCountyParser() {
    super(new OHUnionCountyAParser(),
          new OHUnionCountyBParser());
  }
}
