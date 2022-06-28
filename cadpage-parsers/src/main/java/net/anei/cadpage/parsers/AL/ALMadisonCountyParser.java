package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALMadisonCountyParser extends GroupBestParser {
  
  public ALMadisonCountyParser() {
    super(new ALMadisonCountyAParser(), new ALMadisonCountyBParser(),
          new ALMadisonCountyCParser());
  }
}
