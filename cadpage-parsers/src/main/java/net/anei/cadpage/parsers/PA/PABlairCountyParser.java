package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Blair County, PA
 */


public class PABlairCountyParser extends GroupBestParser {
  
  public PABlairCountyParser() {
    super(new PABlairCountyAParser(), new PABlairCountyBParser());
  }
}
