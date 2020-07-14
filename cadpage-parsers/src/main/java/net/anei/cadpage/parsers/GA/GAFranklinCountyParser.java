package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Franklin County, GA
 */
public class GAFranklinCountyParser extends GroupBestParser {

  public GAFranklinCountyParser() {
    super(new GAFranklinCountyAParser(), new GAFranklinCountyBParser());
  }
}
