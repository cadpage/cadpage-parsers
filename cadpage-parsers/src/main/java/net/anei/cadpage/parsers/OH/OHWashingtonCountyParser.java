package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Washington County, OH
 */

public class OHWashingtonCountyParser extends GroupBestParser {
  
  public OHWashingtonCountyParser() {
    super(new OHWashingtonCountyAParser(),
          new OHWashingtonCountyBParser());
  }
}
