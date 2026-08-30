package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALTuscaloosaCountyParser extends GroupBestParser {

  public ALTuscaloosaCountyParser() {
    super(new ALTuscaloosaCountyAParser(), new ALTuscaloosaCountyBParser());
  }
}
