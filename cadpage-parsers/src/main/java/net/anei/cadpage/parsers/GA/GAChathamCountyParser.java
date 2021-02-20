package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Chatham County, GA
 */
public class GAChathamCountyParser extends GroupBestParser {

  public GAChathamCountyParser() {
    super(new GAChathamCountyAParser(), new GAChathamCountyBParser());
  }
}
