package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Camden County, GA
 */
public class GACamdenCountyParser extends GroupBestParser {
  
  public GACamdenCountyParser() {
    super(new GACamdenCountyAParser(), new GACamdenCountyBParser());
  }
}
