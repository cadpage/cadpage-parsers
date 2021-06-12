package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYJeffersonCountyParser extends GroupBestParser {

  public NYJeffersonCountyParser() {
    super(new NYJeffersonCountyAParser(),
          new NYJeffersonCountyBParser(),
          new NYJeffersonCountyCParser());
  }
}
