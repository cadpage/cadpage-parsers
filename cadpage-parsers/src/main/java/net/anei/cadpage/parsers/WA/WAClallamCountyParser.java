package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Clallam County, WA
 */
public class WAClallamCountyParser extends GroupBestParser {

  public WAClallamCountyParser() {
    super(new WAClallamCountyAParser(),
          new WAClallamCountyBParser(),
          new WAClallamCountyCParser(),
          new WAClallamCountyDParser());
  }
}
