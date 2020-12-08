package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Shelby County, OH
 */

public class OHShelbyCountyParser extends GroupBestParser {

  public OHShelbyCountyParser() {
    super(new OHShelbyCountyAParser(), new OHShelbyCountyBParser());
  }
}
