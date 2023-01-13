package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Yuba County, CA
 */
public class CAYubaCountyParser extends GroupBestParser {
  public CAYubaCountyParser() {
    super(new CAYubaCountyAParser(), new CAYubaCountyBParser());
  }
}
