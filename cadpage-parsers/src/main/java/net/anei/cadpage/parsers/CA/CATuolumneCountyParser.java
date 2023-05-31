package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Tuolumne County, CA
 */
public class CATuolumneCountyParser extends GroupBestParser {
  public CATuolumneCountyParser() {
    super(new CATuolumneCountyBParser());
  }
}
