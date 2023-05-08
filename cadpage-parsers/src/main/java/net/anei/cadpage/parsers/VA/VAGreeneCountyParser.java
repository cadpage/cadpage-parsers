package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Greene County, VA
 */
public class VAGreeneCountyParser extends GroupBestParser {

  public VAGreeneCountyParser() {
    super(new VAGreeneCountyAParser(), new VAGreeneCountyBParser());
  }
}
