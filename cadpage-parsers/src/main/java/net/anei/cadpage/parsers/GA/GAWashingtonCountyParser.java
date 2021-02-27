package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Washington County, GA
 */
public class GAWashingtonCountyParser extends GroupBestParser {

  public GAWashingtonCountyParser() {
    super(new GAWashingtonCountyAParser(), new GAWashingtonCountyBParser());
  }
}
