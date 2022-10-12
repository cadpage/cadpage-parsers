package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALTalladegaCountyParser extends GroupBestParser {

  public ALTalladegaCountyParser() {
    super(new ALTalladegaCountyAParser(), new ALTalladegaCountyBParser());
  }
}
