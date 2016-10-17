package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYCortlandCountyParser extends GroupBestParser {
  
  public NYCortlandCountyParser() {
    super(new NYCortlandCountyAParser(), new NYCortlandCountyBParser(),
        new NYCortlandCountyCParser());
  }
}
