package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Richland County, OH
 */

public class OHRichlandCountyParser extends GroupBestParser {
  
  public OHRichlandCountyParser() {
    super(new OHRichlandCountyAParser(), new OHRichlandCountyBParser());
  }
}
