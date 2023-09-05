package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Cherokee County, GA
 */
public class GACherokeeCountyParser extends GroupBestParser {

  public GACherokeeCountyParser() {
    super(new GACherokeeCountyAParser(), new GACherokeeCountyBParser());
  }
}
