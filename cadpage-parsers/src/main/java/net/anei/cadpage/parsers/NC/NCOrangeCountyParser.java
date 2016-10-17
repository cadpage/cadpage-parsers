package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCOrangeCountyParser extends GroupBestParser {
  
  public NCOrangeCountyParser() {
    super(new NCOrangeCountyAParser(), new NCOrangeCountyBParser());
  }
}
