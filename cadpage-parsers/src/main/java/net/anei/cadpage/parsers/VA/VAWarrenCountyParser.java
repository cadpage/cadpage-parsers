package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Warren County, VA
 */
public class VAWarrenCountyParser extends GroupBestParser {

  public VAWarrenCountyParser() {
    super(new VAWarrenCountyAParser(), new VAWarrenCountyBParser());
  }
}
