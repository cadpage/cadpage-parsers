package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCForsythCountyParser extends GroupBestParser {

  public NCForsythCountyParser() {
    super(new NCForsythCountyAParser(), new NCForsythCountyBParser());
  }
}
