package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALButlerCountyParser extends GroupBestParser {

  public ALButlerCountyParser() {
    super(new ALButlerCountyAParser(), new ALButlerCountyBParser());
  }
}
