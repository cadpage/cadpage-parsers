package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.GroupBestParser;


public class MSTateCountyParser extends GroupBestParser {

  public MSTateCountyParser() {
    super(new MSTateCountyAParser(), new MSTateCountyBParser());
  }
}
