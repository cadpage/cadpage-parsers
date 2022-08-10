package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALWinstonCountyParser extends GroupBestParser {

  public ALWinstonCountyParser() {
    super(new ALWinstonCountyAParser(), new ALWinstonCountyBParser());
  }
}
