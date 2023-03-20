package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALTallapoosaCountyParser extends GroupBestParser {

  public ALTallapoosaCountyParser() {
    super(new ALTallapoosaCountyAParser(), new ALTallapoosaCountyBParser());
  }
}
