package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDSaintMarysCountyParser extends GroupBestParser {

  public MDSaintMarysCountyParser() {
    super(new MDSaintMarysCountyAParser(),
          new MDSaintMarysCountyBParser(),
          new MDSaintMarysCountyCParser());
  }
}
