package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Stanislaus County, CA
 */
public class CAStanislausCountyParser extends GroupBestParser {
  public CAStanislausCountyParser() {
    super(new CAStanislausCountyAParser(), new CAStanislausCountyBParser());
  }
}
