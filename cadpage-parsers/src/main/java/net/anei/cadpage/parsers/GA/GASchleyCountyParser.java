package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Schley County, GA
 */
public class GASchleyCountyParser extends GroupBestParser {
  
  public GASchleyCountyParser() {
    super(new GASchleyCountyAParser(), new GASchleyCountyBParser());
  }
}
