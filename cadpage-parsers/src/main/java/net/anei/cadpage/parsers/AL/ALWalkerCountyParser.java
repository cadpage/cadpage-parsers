package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALWalkerCountyParser extends GroupBestParser {

  public ALWalkerCountyParser() {
    super(new ALWalkerCountyAParser(), new ALWalkerCountyBParser());
  }
}
