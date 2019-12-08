package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCPenderCountyParser extends GroupBestParser {
  
  public NCPenderCountyParser() {
    super(new NCPenderCountyAParser(), new NCPenderCountyBParser());
  }
}
