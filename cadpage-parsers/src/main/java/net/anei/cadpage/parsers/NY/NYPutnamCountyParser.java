package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYPutnamCountyParser extends GroupBestParser {

  public NYPutnamCountyParser() {
    super(new NYPutnamCountyAParser(),
          new NYPutnamCountyBParser());
  }
}
