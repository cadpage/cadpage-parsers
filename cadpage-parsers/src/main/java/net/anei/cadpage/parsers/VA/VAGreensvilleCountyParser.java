package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Greensville County, VA
 */
public class VAGreensvilleCountyParser extends GroupBestParser {

  public VAGreensvilleCountyParser() {
    super(new VAGreensvilleCountyAParser(), new VAGreensvilleCountyBParser());
  }
}
