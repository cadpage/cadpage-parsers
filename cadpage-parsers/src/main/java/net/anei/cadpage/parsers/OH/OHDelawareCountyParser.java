package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Delaware County, OH
 */

public class OHDelawareCountyParser extends GroupBestParser {

  public OHDelawareCountyParser() {
    super(new OHDelawareCountyAParser(), new OHDelawareCountyBParser());
  }
}
