package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Taylor County, GA
 */
public class GATaylorCountyParser extends GroupBestParser {

  public GATaylorCountyParser() {
    super(new GATaylorCountyAParser(), new GATaylorCountyBParser());
  }
}
