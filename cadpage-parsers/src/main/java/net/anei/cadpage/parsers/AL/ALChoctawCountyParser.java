package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALChoctawCountyParser extends GroupBestParser {

  public ALChoctawCountyParser() {
    super(new ALChoctawCountyAParser(), new ALChoctawCountyBParser());
  }
}
