package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Butler County, PA
 */


public class PAButlerCountyParser extends GroupBestParser {
  
  public PAButlerCountyParser() {
    super(new PAButlerCountyAParser(), new PAButlerCountyBParser());
  }
}
