package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALEtowahCountyParser extends GroupBestParser {

  public ALEtowahCountyParser() {
    super(new ALEtowahCountyAParser(),
          new ALEtowahCountyBParser(),
          new ALEtowahCountyCParser(),
          new ALEtowahCountyDParser());
  }
}
