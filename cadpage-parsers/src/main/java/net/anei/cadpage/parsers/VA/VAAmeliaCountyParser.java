package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Amelia County, VA
 */
public class VAAmeliaCountyParser extends GroupBestParser {
  
  public VAAmeliaCountyParser() {
    super(new VAAmeliaCountyAParser(), new VAAmeliaCountyBParser());
  }
}
