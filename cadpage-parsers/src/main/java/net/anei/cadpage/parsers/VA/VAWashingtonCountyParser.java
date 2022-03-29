package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Washington County, VA
 */
public class VAWashingtonCountyParser extends GroupBestParser {

  public VAWashingtonCountyParser() {
    super(new VAWashingtonCountyAParser(), new VAWashingtonCountyBParser());
  }
}
