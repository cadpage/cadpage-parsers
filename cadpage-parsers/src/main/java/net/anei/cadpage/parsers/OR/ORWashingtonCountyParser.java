package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Washington County, OR
 * Also Clackamas County
 */
public class ORWashingtonCountyParser extends GroupBestParser {
  public ORWashingtonCountyParser() {
    super(new ORWashingtonCountyAParser(), 
          new ORWashingtonCountyBParser(), 
          new ORWashingtonCountyCParser(),
          new ORWashingtonCountyDParser());
  }
}
