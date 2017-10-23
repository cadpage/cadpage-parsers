package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYCayugaCountyParser extends GroupBestParser {
  
  public NYCayugaCountyParser() {
    super(new NYCayugaCountyAParser(), new NYCayugaCountyBParser(),
          new NYCayugaCountyCParser());
  }
}
