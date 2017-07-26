package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCCherokeeCountyParser extends GroupBestParser {
  
  public NCCherokeeCountyParser() {
    super(new NCCherokeeCountyAParser(), new NCCherokeeCountyBParser());
  }
}
