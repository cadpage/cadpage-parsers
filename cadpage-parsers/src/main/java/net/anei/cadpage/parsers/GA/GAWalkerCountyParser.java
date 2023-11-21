package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Walker County, GA
 */
public class GAWalkerCountyParser extends GroupBestParser {

  public GAWalkerCountyParser() {
    super(new GAWalkerCountyAParser(), new GAWalkerCountyBParser());
  }
}
