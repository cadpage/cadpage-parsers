package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Effingham County, GA
 */
public class GAEffinghamCountyParser extends GroupBestParser {
  
  public GAEffinghamCountyParser() {
    super(new GAEffinghamCountyAParser(), new GAEffinghamCountyBParser());
  }
}
