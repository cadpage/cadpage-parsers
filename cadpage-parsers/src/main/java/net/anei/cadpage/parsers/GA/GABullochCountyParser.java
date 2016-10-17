package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Bulloch County, GA
 */
public class GABullochCountyParser extends GroupBestParser {
  
  public GABullochCountyParser() {
    super(new GABullochCountyAParser(), new GABullochCountyBParser());
  }
}
