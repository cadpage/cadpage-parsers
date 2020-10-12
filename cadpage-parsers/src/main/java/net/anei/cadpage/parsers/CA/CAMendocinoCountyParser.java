package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Mendocino County, CA
 */
public class CAMendocinoCountyParser extends GroupBestParser {
  public CAMendocinoCountyParser() {
    super(new CAMendocinoCountyAParser(), new CAMendocinoCountyBParser(), new CAMendocinoCountyCParser());
  }
}
