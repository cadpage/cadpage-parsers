package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Monroe County, PA
 */


public class PAMonroeCountyParser extends GroupBestParser {
  
  public PAMonroeCountyParser() {
    super(new PAMonroeCountyAParser(),
          new PAMonroeCountyBParser());
  }
}
