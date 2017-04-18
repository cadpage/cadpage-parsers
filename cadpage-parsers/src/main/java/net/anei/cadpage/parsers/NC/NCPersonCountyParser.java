package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCPersonCountyParser extends GroupBestParser {
  
  public NCPersonCountyParser() {
    super(new NCPersonCountyAParser(), new NCPersonCountyBParser());
  }
}
