package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * South Lake Tahoe County, CA
 */
public class CASouthLakeTahoeParser extends GroupBestParser {
  public CASouthLakeTahoeParser() {
    super(new CASouthLakeTahoeAParser(), new CASouthLakeTahoeBParser());
  }
}
