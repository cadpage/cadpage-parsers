package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVBooneCountyParser extends GroupBestParser {
  
  public WVBooneCountyParser() {
    super(new WVBooneCountyAParser(), 
          new WVBooneCountyBParser(),
          new WVBooneCountyCParser());
  }
}
