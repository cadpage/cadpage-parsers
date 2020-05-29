package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCMadisonCountyParser extends GroupBestParser {
  
  public NCMadisonCountyParser() {
    super(new NCMadisonCountyAParser(), new NCMadisonCountyBParser());
  }
}
