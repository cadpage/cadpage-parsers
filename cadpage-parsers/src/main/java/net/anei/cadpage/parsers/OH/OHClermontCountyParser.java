package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Clermont County, OH
 */

public class OHClermontCountyParser extends GroupBestParser {
  
  public OHClermontCountyParser() {
    super(new OHClermontCountyAParser(), new OHClermontCountyBParser());
  }
}
