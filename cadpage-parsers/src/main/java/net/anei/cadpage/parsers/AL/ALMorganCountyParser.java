package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALMorganCountyParser extends GroupBestParser {

  public ALMorganCountyParser() {
    super(new ALMorganCountyAParser(), new ALMorganCountyBParser());
  }
}
