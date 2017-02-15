package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Dade County, GA
 */
public class GADadeCountyParser extends GroupBestParser {
  
  public GADadeCountyParser() {
    super(new GADadeCountyAParser(), new GADadeCountyBParser());
  }
}
