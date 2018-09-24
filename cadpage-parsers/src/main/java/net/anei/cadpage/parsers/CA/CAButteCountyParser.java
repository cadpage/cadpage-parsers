package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Butte County, CA
 */
public class CAButteCountyParser extends GroupBestParser {
  public CAButteCountyParser() {
    super(new CAButteCountyAParser(),
          new CAButteCountyBParser(),
          new CAButteCountyCParser());
  }
}
