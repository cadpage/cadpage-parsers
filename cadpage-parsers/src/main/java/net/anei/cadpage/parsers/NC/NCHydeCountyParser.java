package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCHydeCountyParser extends GroupBestParser {
  
  public NCHydeCountyParser() {
    super(new NCHydeCountyAParser(), 
          new NCHydeCountyBParser(),
          new NCHydeCountyCParser());
  }
}
