package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Knox County, OH
 */

public class OHKnoxCountyParser extends GroupBestParser {

  public OHKnoxCountyParser() {
    super(new OHKnoxCountyAParser(), new OHKnoxCountyBParser());
  }
}
