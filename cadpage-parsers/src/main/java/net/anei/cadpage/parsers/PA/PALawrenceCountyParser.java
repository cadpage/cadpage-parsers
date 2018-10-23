package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Lawrence County, PA
 */


public class PALawrenceCountyParser extends GroupBestParser {
  
  public PALawrenceCountyParser() {
    super(new PALawrenceCountyAParser(), new PALawrenceCountyBParser());
  }
}
