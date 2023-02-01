package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Lawrence County, OH
 */

public class OHLawrenceCountyParser extends GroupBestParser {

  public OHLawrenceCountyParser() {
    super(new OHLawrenceCountyAParser(), new OHLawrenceCountyBParser());
  }
}
