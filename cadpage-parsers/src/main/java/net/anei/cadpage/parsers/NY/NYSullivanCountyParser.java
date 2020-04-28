package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYSullivanCountyParser extends GroupBestParser {
  
  public NYSullivanCountyParser() {
    super(new NYSullivanCountyAParser(),
          new NYSullivanCountyBParser(),
          new NYSullivanCountyCParser());
  }
}
