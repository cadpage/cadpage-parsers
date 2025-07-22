package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Washington County, AR
 */
public class ARWashingtonCountyParser extends GroupBestParser {
  public ARWashingtonCountyParser() {
    super(new ARWashingtonCountyAParser(),
          new ARWashingtonCountyBParser(),
          new ARWashingtonCountyCParser());
  }
}
