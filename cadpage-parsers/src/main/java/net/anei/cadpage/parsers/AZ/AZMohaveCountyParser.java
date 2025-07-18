package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Mohave County, AZ
 */
public class AZMohaveCountyParser extends GroupBestParser {
  public AZMohaveCountyParser() {
    super(new AZMohaveCountyAParser(), new AZMohaveCountyBParser());
  }
}
