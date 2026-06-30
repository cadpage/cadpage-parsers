package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Kern County, CA
 */
public class CAKernCountyParser extends GroupBestParser {
  public CAKernCountyParser() {
    super(new CAKernCountyAParser(), new CAKernCountyBParser());
  }
}
