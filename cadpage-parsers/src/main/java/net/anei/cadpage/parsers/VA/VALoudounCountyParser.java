package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Loudoun County, VA
 */
public class VALoudounCountyParser extends GroupBestParser {

  public VALoudounCountyParser() {
    super(new VALoudounCountyBParser(), new VALoudounCountyCParser());
  }
}
