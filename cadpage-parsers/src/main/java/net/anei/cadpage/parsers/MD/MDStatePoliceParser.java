package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDStatePoliceParser extends GroupBestParser {

  public MDStatePoliceParser() {
    super(new MDStatePoliceAParser(), new MDStatePoliceBParser());
  }
}
