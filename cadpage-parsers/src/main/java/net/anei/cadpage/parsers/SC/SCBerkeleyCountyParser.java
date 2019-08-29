package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCBerkeleyCountyParser extends GroupBestParser {
  
  public SCBerkeleyCountyParser() {
    super(new SCBerkeleyCountyAParser(), new SCBerkeleyCountyBParser());
  }
}
