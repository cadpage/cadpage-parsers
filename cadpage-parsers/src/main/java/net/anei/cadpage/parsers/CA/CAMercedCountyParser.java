package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Merced County, CA
 */
public class CAMercedCountyParser extends GroupBestParser {
  public CAMercedCountyParser() {
    super(new CAMercedCountyAParser(), new CAMercedCountyBParser());
  }
}
