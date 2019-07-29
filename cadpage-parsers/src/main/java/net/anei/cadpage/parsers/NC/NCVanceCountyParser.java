package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCVanceCountyParser extends GroupBestParser {
  
  public NCVanceCountyParser() {
    super(new NCVanceCountyAParser(), new NCVanceCountyCParser());
  }
}
