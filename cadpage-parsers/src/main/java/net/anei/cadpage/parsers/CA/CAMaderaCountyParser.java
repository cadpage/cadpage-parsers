package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Madera County, CA
 */
public class CAMaderaCountyParser extends GroupBestParser {
  public CAMaderaCountyParser() {
    super(new CAMaderaCountyAParser(), new CAMaderaCountyBParser());
  }
}
