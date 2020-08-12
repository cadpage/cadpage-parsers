package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDKentCountyParser extends GroupBestParser {

  public MDKentCountyParser() {
    super(new MDKentCountyAParser(),
           new MDKentCountyBParser(),
           new MDKentCountyCParser());
  }

}
