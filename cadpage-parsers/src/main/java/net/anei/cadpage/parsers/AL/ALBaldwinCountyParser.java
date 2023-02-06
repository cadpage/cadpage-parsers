package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALBaldwinCountyParser extends GroupBestParser {

  public ALBaldwinCountyParser() {
    super(new ALBaldwinCountyBParser(), new ALBaldwinCountyDParser());
  }
}
