package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Decatur County, GA
 */
public class GADecaturCountyParser extends GroupBestParser {
  
  public GADecaturCountyParser() {
    super(new GADecaturCountyAParser(), 
          new GADecaturCountyBParser(),
          new GADecaturCountyCParser());
  }
}
