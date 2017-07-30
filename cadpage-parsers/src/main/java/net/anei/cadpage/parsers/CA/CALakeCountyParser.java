package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Los Angeles County, CA
 */
public class CALakeCountyParser extends GroupBestParser {
  public CALakeCountyParser() {
    super(new CALakeCountyAParser(), new CALakeCountyBParser());
  }
}
