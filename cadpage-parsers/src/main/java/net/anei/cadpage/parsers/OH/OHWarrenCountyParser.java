package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Warren County, OH
 */

public class OHWarrenCountyParser extends GroupBestParser {
  
  public OHWarrenCountyParser() {
    super(new OHWarrenCountyAParser(),
          new OHWarrenCountyBParser(),
          new OHWarrenCountyCParser(),
          new OHWarrenCountyDParser(),
          new OHWarrenCountyEParser());
  }
}
