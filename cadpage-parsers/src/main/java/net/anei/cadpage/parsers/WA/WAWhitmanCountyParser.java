package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Whitman County, WA
 */
public class WAWhitmanCountyParser extends GroupBestParser {

  public WAWhitmanCountyParser() {
    super(new WAWhitmanCountyBParser());
  }
}
