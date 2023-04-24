package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Los Angeles County, CA
 */
public class CALosAngelesCountyParser extends GroupBestParser {
  public CALosAngelesCountyParser() {
    super(new CALosAngelesCountyAParser(), new CALosAngelesCountyBParser(),
          new CALosAngelesCountyCParser(), new CALosAngelesCountyGParser());
  }
}
