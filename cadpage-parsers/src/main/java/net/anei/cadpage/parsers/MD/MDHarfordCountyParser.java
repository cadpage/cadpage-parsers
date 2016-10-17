package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDHarfordCountyParser extends GroupBestParser {
  
  public MDHarfordCountyParser() {
    super(new MDHarfordCountyAParser(),
           new MDHarfordCountyBParser());
  }
  
}
