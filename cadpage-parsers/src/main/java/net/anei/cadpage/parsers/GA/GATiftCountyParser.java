package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Tifton County, GA
 */
public class GATiftCountyParser extends GroupBestParser {
  
  public GATiftCountyParser() {
    super(new GATiftCountyAParser(), new GATiftCountyBParser());
  }
}
