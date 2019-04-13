package net.anei.cadpage.parsers.ZSE;

import net.anei.cadpage.parsers.GroupBestParser;

public class ZSESwedenParser extends GroupBestParser {
  public ZSESwedenParser() {
    super(new ZSESwedenAParser(), 
          new ZSESwedenBParser(), 
          new ZSESwedenCParser(),
          new ZSESwedenDParser());
  }
}
