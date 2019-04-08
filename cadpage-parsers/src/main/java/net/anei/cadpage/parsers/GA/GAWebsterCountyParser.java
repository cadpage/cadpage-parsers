package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Webster County, GA
 */
public class GAWebsterCountyParser extends GroupBestParser {
  
  public GAWebsterCountyParser() {
    super(new GAWebsterCountyAParser(), new GAWebsterCountyBParser());
  }
}
