package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALChiltonCountyParser extends GroupBestParser {

  public ALChiltonCountyParser() {
    super(new ALChiltonCountyAParser(), new ALChiltonCountyBParser());
  }
}
