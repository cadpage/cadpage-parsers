package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Monterey County, CA
 */
public class CAMontereyCountyParser extends GroupBestParser {
  public CAMontereyCountyParser() {
    super(new CAMontereyCountyAParser(), new CAMontereyCountyBParser());
  }
}
