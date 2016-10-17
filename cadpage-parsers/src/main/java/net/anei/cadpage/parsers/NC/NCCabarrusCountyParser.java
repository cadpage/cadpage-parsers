package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCCabarrusCountyParser extends GroupBestParser {
  
  public NCCabarrusCountyParser() {
    super(new NCCabarrusCountyAParser(), new NCCabarrusCountyBParser());
  }
}
