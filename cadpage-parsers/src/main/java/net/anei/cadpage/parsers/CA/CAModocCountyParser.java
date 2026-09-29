package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Modoc County, CA
 */
public class CAModocCountyParser extends GroupBestParser {
  public CAModocCountyParser() {
    super(new CAModocCountyAParser(), new CAModocCountyBParser());
  }
}
