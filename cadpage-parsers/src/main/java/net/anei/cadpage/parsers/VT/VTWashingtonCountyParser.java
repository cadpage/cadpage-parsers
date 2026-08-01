package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Washington County, VT
 */
public class VTWashingtonCountyParser extends GroupBestParser {

  public VTWashingtonCountyParser() {
    super(new VTWashingtonCountyAParser(), new VTWashingtonCountyBParser());
  }
}
