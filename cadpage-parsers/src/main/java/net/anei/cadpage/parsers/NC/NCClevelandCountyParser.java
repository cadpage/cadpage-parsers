package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCClevelandCountyParser extends GroupBestParser {
  
  public NCClevelandCountyParser() {
    super(new NCClevelandCountyAParser(), new NCClevelandCountyBParser());
  }
}
