package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Adams County, PA
 */


public class PAAdamsCountyParser extends GroupBestParser {
  
  public PAAdamsCountyParser() {
    super(new PAAdamsCountyAParser(), new PAAdamsCountyBParser());
  }
}
