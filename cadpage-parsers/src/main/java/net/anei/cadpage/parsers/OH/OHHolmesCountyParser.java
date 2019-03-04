package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Holmes County, OH
 */

public class OHHolmesCountyParser extends GroupBestParser {
  
  public OHHolmesCountyParser() {
    super(new OHHolmesCountyAParser(), new OHHolmesCountyBParser());
  }
}
