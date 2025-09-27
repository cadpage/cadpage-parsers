package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Roanoke County, VA
 */
public class VARoanokeCountyParser extends GroupBestParser {

  public VARoanokeCountyParser() {
    super(new VARoanokeCountyAParser(),
          new VARoanokeCountyBParser());
  }
}
