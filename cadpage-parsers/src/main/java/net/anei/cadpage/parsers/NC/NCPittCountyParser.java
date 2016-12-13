package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCPittCountyParser extends GroupBestParser {
  
  public NCPittCountyParser() {
    super(new NCPittCountyAParser(), new NCPittCountyBParser());
  }
}
