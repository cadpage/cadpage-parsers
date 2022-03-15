package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALWashingtonCountyParser extends GroupBestParser {

  public ALWashingtonCountyParser() {
    super(new ALWashingtonCountyAParser(), new ALWashingtonCountyBParser());
  }
}
