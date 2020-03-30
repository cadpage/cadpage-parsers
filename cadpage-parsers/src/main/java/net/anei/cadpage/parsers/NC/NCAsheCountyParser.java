package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCAsheCountyParser extends GroupBestParser {
  
  public NCAsheCountyParser() {
    super(new NCAsheCountyAParser(), new NCAsheCountyBParser());
  }
}
