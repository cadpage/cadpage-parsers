package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALColbertCountyParser extends GroupBestParser {

  public ALColbertCountyParser() {
    super(new ALColbertCountyAParser(),
          new ALColbertCountyCParser());
  }
}
