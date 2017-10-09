package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Fannin County, GA
 */
public class GAFanninCountyParser extends GroupBestParser {
  
  public GAFanninCountyParser() {
    super(new GAFanninCountyAParser(), new GAFanninCountyBParser());
  }
}
