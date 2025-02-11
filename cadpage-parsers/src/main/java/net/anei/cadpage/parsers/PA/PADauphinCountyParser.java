package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Dauphin County, PA
 */


public class PADauphinCountyParser extends GroupBestParser {

  public PADauphinCountyParser() {
    super(new PADauphinCountyAParser(),
          new PADauphinCountyBParser(),
          new PADauphinCountyCParser(),
          new PADauphinCountyDParser());
  }
}
