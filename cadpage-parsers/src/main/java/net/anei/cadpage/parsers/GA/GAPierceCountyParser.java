package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Pierce County, GA
 */
public class GAPierceCountyParser extends GroupBestParser {

  public GAPierceCountyParser() {
    super(new GAPierceCountyAParser(), new GAPierceCountyBParser());
  }
}
