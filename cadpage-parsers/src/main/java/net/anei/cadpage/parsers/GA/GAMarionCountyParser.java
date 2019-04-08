package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Marion County, GA
 */
public class GAMarionCountyParser extends GroupBestParser {
  
  public GAMarionCountyParser() {
    super(new GAMarionCountyAParser(), new GAMarionCountyBParser());
  }
}
