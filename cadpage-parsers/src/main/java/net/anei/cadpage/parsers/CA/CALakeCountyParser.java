package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Lake County, CA
 */
public class CALakeCountyParser extends GroupBestParser {
  public CALakeCountyParser() {
    super(new CALakeCountyAParser(), new CALakeCountyBParser());
  }
}
