package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Fayette County, PA
 */


public class PAFayetteCountyParser extends GroupBestParser {
  
  public PAFayetteCountyParser() {
    super(new PAFayetteCountyAParser(),
           new PAFayetteCountyCParser());
  }
}
