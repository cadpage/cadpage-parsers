package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Licking County, OH
 */

public class OHLickingCountyParser extends GroupBestParser {

  public OHLickingCountyParser() {
    super(new OHLickingCountyAParser(), new OHLickingCountyBParser(),
          new OHLickingCountyCParser());
  }
}
